package deh.methods;

import java.util.LinkedList;

import deh.ballot.RankedChoiceBallot;

public class CycleFreeCondorcet extends RankedChoiceMethod{
	public CycleFreeCondorcet(RankedChoiceBallot[] ballots, String[] candidateList) {
		super(ballots, candidateList);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String[] candidates = {"A. Tony Grue", "B. Dick Balls", "C. Total Moron"};
        LinkedList<Integer> vote1 = new LinkedList<Integer>();
        vote1.add(0); vote1.add(1); vote1.add(2);//9x A>B>C
        RankedChoiceBallot voter1 = new RankedChoiceBallot(vote1, candidates);
        LinkedList<Integer> vote2 = new LinkedList<Integer>();//12x B>C>A
        vote2.add(1); vote2.add(2); vote2.add(0);
        RankedChoiceBallot voter2 = new RankedChoiceBallot(vote2, candidates);
        LinkedList<Integer> vote3 = new LinkedList<Integer>();//8x C>A>B
        vote3.add(2); vote3.add(0); vote3.add(1);
        RankedChoiceBallot voter3 = new RankedChoiceBallot(vote3, candidates);
        RankedChoiceBallot[] box = new RankedChoiceBallot[29];
        for(int i = 0; i < 9; i++) {
            box[i]=voter1;
        }
        for(int i = 0; i < 12; i++) {
            box[i+9]=voter2;
        }
        for(int i = 0; i < 8; i++) {
            box[i+21]=voter3;
        }
        CycleFreeCondorcet BadIRVCase = new CycleFreeCondorcet(box, candidates);
        System.out.println(BadIRVCase.verbose());
        RankedChoiceBallot[] liarBox = new RankedChoiceBallot[19];
        vote1.clear();
        vote1.add(0); vote1.add(1); vote1.add(2);
        vote2.clear();
        vote2.add(1); vote2.add(2); vote2.add(0);
        vote3.clear();
        vote3.add(2); vote3.add(0); vote3.add(1);
      
        for(int i = 0; i < 5; i++) {
            liarBox[i]=voter1;
        }
        for(int i = 0; i < 8; i++) {
            liarBox[i+5]=voter2;
        }
        for(int i = 0; i < 6; i++) {
            liarBox[i+13]=voter3;
        }
        CycleFreeCondorcet lyingPays = new CycleFreeCondorcet(liarBox, candidates);
        System.out.println(lyingPays.verbose());
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
