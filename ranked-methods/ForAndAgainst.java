package election.methods;

import java.util.Random;

import election.ballot.RankedChoiceBallot;

public class ForAndAgainst extends RankedChoiceMethod{

    public ForAndAgainst(RankedChoiceBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getWinner() {
        Random gen = new Random();
        double tiebreakPriority = gen.nextDouble();
    	RankedChoiceBallot[] votes = super.getVotes();
        int numCandidates = super.getCandidates().length;
        int[][] preferenceTable = super.getPreferenceTable(votes, numCandidates);
        int winner = 0;
        int highestTotal = 0;
        for(int i = 0; i < numCandidates; i++){
        	int netScore = preferenceTable[i][0]-preferenceTable[i][numCandidates-1];
            if(netScore > highestTotal){
                winner = i;
                highestTotal = netScore;
                tiebreakPriority = gen.nextDouble();
            }
            else{
            	if(netScore == highestTotal) {
            		double secondTiebreakPriority = gen.nextDouble();
            		if (tiebreakPriority < secondTiebreakPriority) {
            			tiebreakPriority = secondTiebreakPriority;
            			winner = i;
            			highestTotal = netScore;
            		}
            	}
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
        	int netScore = preferenceTable[i][0]-preferenceTable[i][numCandidates-1];
        	result += super.getCandidates()[i].toString() + ": " + netScore + "\n";
            if(netScore > highestTotal){
                winner = i;
                highestTotal = preferenceTable[i][0];
            }
        }
        return result;
    }
}
