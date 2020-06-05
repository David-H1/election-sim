package election.methods;

import election.ballot.RankedChoiceBallot;

public class Borda extends RankedChoiceMethod{
    public Borda(RankedChoiceBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getWinner() {
        int numCandidates = super.getCandidates().length;
        int[][] preferenceTable = super.getPreferenceTable(super.getVotes(), numCandidates);
        int[] bordaCount = new int[numCandidates];
        for(int positionValue = 1; positionValue < numCandidates; positionValue++){
            for(int candidate = 0; candidate < numCandidates; candidate++){
                bordaCount[candidate] += positionValue*preferenceTable[candidate][numCandidates-1-positionValue];
            }
        }
        int winner = 0;
        int bestBordaCount = 0;
        for(int candidate = 0; candidate < numCandidates; candidate++){
            if(bordaCount[candidate] > bestBordaCount){
                winner = candidate;
                bestBordaCount = bordaCount[candidate];
            }
        }
        return winner;
    }

	@Override
	public String verbose() {
		// TODO Auto-generated method stub
		return null;
	}
}
