package election.methods;

import election.ballot.ScoreBallot;

public class StarVoting extends RatingMethod{
	public StarVoting(ScoreBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList, 0, 5);
    }

    @Override
    public int getWinner() {
        int[] score = super.getScoreSum();
        int numCandidates = super.getCandidates().length;
        int first_place = 0;
        int lead_score = 0;
        int second_place = 0;
        int second_score = 0;
        for(int cand = 0; cand < numCandidates; cand++){
            if(score[cand] > lead_score){
                second_place = first_place;
                second_score = lead_score;
            	first_place = cand;
                lead_score = score[cand];
            }
            else {
            	if(score[cand] > second_score) {
            		second_place = cand;
            		second_score = score[cand];
            	}
            }
        }
        int votes_lead = 0;
        int votes_rup = 0;
        ScoreBallot[] box = super.getVotes();
        for(int i = 0; i < box.length; i++) {
        	if(box[i].getScores()[first_place] > box[i].getScores()[second_place]) {
        		votes_lead++;
        	}
        	else {
        		if(box[i].getScores()[first_place] < box[i].getScores()[second_place]) {
            		votes_rup++;
            	}
        	}
        }
        if(votes_rup > votes_lead) {
        	return second_place;
        }
        else {
        	return first_place;
        }
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
