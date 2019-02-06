package dh.methods;

import java.util.Arrays;

import dh.ballot.RankedChoiceBallot;

public class BestRivalIRV extends RankedChoiceMethod{

	public BestRivalIRV(RankedChoiceBallot[] ballots, String[] candidateList) {
		super(ballots, candidateList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getWinner() {
		int[][] pairwiseTable = super.getPairwiseTable(super.votes);
		InstantRunoffVoting irv = new InstantRunoffVoting(super.votes.clone(), super.candidates.clone());
		int irvWinner = irv.getWinner();
		int bestCand = irvWinner;
		int bestMargin = 0;
		for(int i = 0; i < pairwiseTable.length; i++) {
			int pairwiseFor = pairwiseTable[i][irvWinner];
			int pairwiseAgainst = pairwiseTable[irvWinner][i];
			int pairwiseMargin = pairwiseFor - pairwiseAgainst;
			if(pairwiseMargin > bestMargin) {
				bestCand = i;
				bestMargin = pairwiseMargin;
			}
		}
		return bestCand;
	}
	
	public String verbose() {
		int[][] pairwiseTable = super.getPairwiseTable(super.votes);
		String result = "IRV Stage:\n";
		InstantRunoffVoting irv = new InstantRunoffVoting(super.votes, super.candidates);
		result += irv.verbose();
		result = result.substring(0, result.lastIndexOf("W"));
		int irvWinner = irv.getWinner();
		result += "IRV Winner:" + irvWinner; 
		result += "\nPairwise matchups:\n";
		int bestCand = irvWinner;
		int bestMargin = 0;
		int[] pairwiseResults = new int[pairwiseTable.length];
		for(int i = 0; i < pairwiseTable.length; i++) {
			int pairwiseFor = pairwiseTable[i][irvWinner];
			int pairwiseAgainst = pairwiseTable[irvWinner][i];
			int pairwiseMargin = pairwiseFor - pairwiseAgainst;
			pairwiseResults[i] = pairwiseMargin;
			if(pairwiseMargin > bestMargin) {
				bestCand = i;
				bestMargin = pairwiseMargin;
			}
			System.out.println(Arrays.toString(pairwiseTable[i]));
		}
		result += Arrays.toString(pairwiseResults);
		result += ("\nWinner:" + bestCand);
		return result;
	}

}
