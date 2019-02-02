package deh.methods;

import deh.ballot.ScoreBallot;

public class ApprovalVoting extends RatingMethod{

    public ApprovalVoting(ScoreBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList, 0, 1);
    }

    @Override
    public int getWinner() {
        int[] timesApproved = super.getScoreSum();
        int numCandidates = super.getCandidates().length;
        int winner = 0;
        int winnerApproval = 0;
        for(int cand = 0; cand < numCandidates; cand++){
            if(timesApproved[cand] > winnerApproval){
                winner = cand;
                winnerApproval = timesApproved[cand];
            }
        }
        return winner;
    }
   
}
