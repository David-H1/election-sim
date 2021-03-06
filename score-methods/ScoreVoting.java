package election.methods;

import election.ballot.ScoreBallot;

public class ScoreVoting extends RatingMethod{
	public ScoreVoting(ScoreBallot[] ballots, String[] candidateList, int minScore, int maxScore) {
        super(ballots, candidateList, minScore, maxScore);
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
