package deh.methods;

import deh.ballot.RankedChoiceBallot;

public class Plurality extends RankedChoiceMethod{

    public Plurality(RankedChoiceBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getWinner() {
        RankedChoiceBallot[] votes = super.getVotes();
        int numCandidates = super.getCandidates().length;
        int[][] preferenceTable = super.getPreferenceTable(votes, numCandidates);
        int winner = 0;
        int highestTotal = 0;
        for(int i = 0; i < numCandidates; i++){
            if(preferenceTable[i][0] > highestTotal){
                winner = i;
                highestTotal = preferenceTable[i][0];
            }
        }
        return winner;
    }
    
    public String verbose() {
    	String result = "";
    	RankedChoiceBallot[] votes = super.getVotes();
        int numCandidates = super.getCandidates().length;
        int[][] preferenceTable = super.getPreferenceTable(votes, numCandidates);
        int winner = 0;
        int highestTotal = 0;
        for(int i = 0; i < numCandidates; i++){
        	result += super.getCandidates()[i].toString() + ": " + preferenceTable[i][0] + "\n";
            if(preferenceTable[i][0] > highestTotal){
                winner = i;
                highestTotal = preferenceTable[i][0];
            }
        }
        return result;
    }

}

