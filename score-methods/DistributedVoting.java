package deh.methods;

import deh.ballot.ScoreBallot;

public class DistributedVoting extends RatingMethod{

	public DistributedVoting(ScoreBallot[] ballots, String[] candidateList, int totalPoints) {
		//Precondition: the sum of the scores on the ballots does not exceed totalPoints.
		//Precondition: there are no negative scores.
		super(ballots, candidateList, 0, totalPoints);
	}

	@Override
	public int getWinner() {
		int numCandidates = super.candidates.length;
		int numVotes = super.votes.length;
		double[][] ballotCopy = new double[numVotes][numCandidates];
		double[] scores = new double[numCandidates]; 
		for(int i = 0; i < numCandidates; i++) {
			for(int j=0; j< numVotes; j++) {
				ballotCopy[j][i] = (double) super.getVotes()[j].getScores()[i];
				scores[i] += ballotCopy[j][i];
			}
		}
		for(int i = 0; i < numCandidates-1; i++) {
			double minScore = Integer.MAX_VALUE;
			int lastPlace = -1;
			for(int j = 0; j < numCandidates; j++) {
				if(scores[j] >= 0.0 & scores[j] < minScore) {
					lastPlace = j;
					minScore = scores[j];
				}
			}
			System.out.print(scores[0] + " " + scores[1] + " " + scores[2] + " " + scores[3] + "\n");
			scores[lastPlace] = -1.0;
			for(double[] vote : ballotCopy) {
				double loserScore = vote[lastPlace];
				vote[lastPlace] = 0.0;
				for(int cand = 0; cand < numCandidates; cand++) {
					if(super.max != loserScore) {
						double transfer = loserScore*vote[cand]/(super.max - loserScore);
						vote[cand] += transfer;
						scores[cand] += transfer;
					}
				}
			}
		}
		int i = 0;
		for(; scores[i] < 0; i++) {}
		return i;
	}
	
}
