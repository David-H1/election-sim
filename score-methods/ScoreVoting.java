package election.methods;

import deh.ballot.ScoreBallot;

public class ScoreVoting extends RatingMethod{
	public ScoreVoting(ScoreBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList, 0, 100);
    }

    @Override
    public int getWinner() {
        int[] score = super.getScoreSum();
        int numCandidates = super.getCandidates().length;
        int winner = 0;
        int winnerScore = 0;
        for(int cand = 0; cand < numCandidates; cand++){
            if(score[cand] > winnerScore){
                winner = cand;
                winnerScore = score[cand];
            }
        }
        return winner;
    }
    
    public String verbose() {
    	String result = "";
    	int[] score = super.getScoreSum();
        int numCandidates = super.getCandidates().length;
        int winner = 0;
        int winnerScore = 0;
        for(int cand = 0; cand < numCandidates; cand++){
            result += super.getCandidates()[cand] + ": " + score[cand] + "\n";
        }
        return result;
    }
   
}
