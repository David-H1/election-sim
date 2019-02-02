package deh.methods;

import java.util.LinkedList;

import deh.ballot.RankedChoiceBallot;

public class CycleFreeCondorcet extends RankedChoiceMethod{
	public CycleFreeCondorcet(RankedChoiceBallot[] ballots, String[] candidateList) {
		super(ballots, candidateList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getWinner() {
		int[][] pairwiseMatrix = super.getPairwiseTable(super.votes);
		boolean winnerFound = false;
		int winner = -1;//If there is no Condorcet winner, 
		for(int candID = 0; !winnerFound && candID < pairwiseMatrix.length; candID++) {
			boolean defeated = false;
			for(int foeID = 0; !defeated && foeID < pairwiseMatrix.length; foeID++) {
				if(candID != foeID) {
					if(pairwiseMatrix[candID][foeID] < pairwiseMatrix[foeID][candID]) {
						defeated = true;
					}
				}
			}
			if(!defeated) {
				winner = candID;
				winnerFound = true;
			}
		}
		return winner;
	}
	
	public String verbose() {
		String result = "";
		int[][] pairwiseMatrix = super.getPairwiseTable(super.getVotes());
		int numCandidates = super.getCandidates().length;
		for(int i = 0; i < super.getCandidates().length; i++) {
			result += "\t";
			result += i;
		}
		result += "\n";
		for(int i = 0; i < super.getCandidates().length; i++) {
			result += i;
			result += "\t";
			for(int j = 0; j < super.getCandidates().length; j++) {
				result += pairwiseMatrix[i][j];
				if(pairwiseMatrix[i][j] > pairwiseMatrix[j][i]) {
					result += "*";
				}
				result += "\t";
			}
			result += "\n";
		}
		result += "Winner:" + getWinner();
		return result;
	}
	
}
