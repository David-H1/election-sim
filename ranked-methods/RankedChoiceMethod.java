package election.methods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import deh.ballot.RankedChoiceBallot;

/*
 * This method outlines the format for a Ranked Choice Method, and contains some
 * useful methods for calculating 
 */

public abstract class RankedChoiceMethod{
    public RankedChoiceBallot[] votes;
    String[] candidates;
  
    public RankedChoiceMethod(RankedChoiceBallot[] ballots, String[] candidateList) {
        votes=ballots;
        candidates=candidateList;
    }
    public abstract int getWinner();
    public String getWinnerName() {
        return candidates[getWinner()];
    }
    public abstract String verbose();
    public static RankedChoiceBallot[] eliminateCandidatesOnBallots(ArrayList<Integer> losers, RankedChoiceBallot[] setOfBallots){//Tool for runoff methods
    	RankedChoiceBallot[] votesWithLosersEliminated = new RankedChoiceBallot[setOfBallots.length];
        for(int i = 0; i < setOfBallots.length; i++) {
        	RankedChoiceBallot voteCopy = (RankedChoiceBallot) setOfBallots[i].clone();
            voteCopy.getRanking().removeAll(losers);
        	votesWithLosersEliminated[i] = voteCopy;
        }
        return votesWithLosersEliminated;
    }
    
    //Weirdly, there was an issue with this method where it wasn't consistently returning the correct answer. What fixed it was making it take the
    //set of ballots as an input argument rather than having it take no argument and return the pairwise table for this.votes,
    //even though the subclasses that use this method to compute the winner just use super.getVotes() as the argument. 
    public int[][] getPairwiseTable(RankedChoiceBallot[] setOfBallots){//Tool for condorcet methods
    	//[i][j] of the output 2D int array is the number of ballots that rank candidate i above candidate j.
        int[][] pairwiseTable = new int[candidates.length][candidates.length];
        ArrayList<Integer> candidatesLeft = new ArrayList<Integer>();//This lists the index numbers of all the candidates.
        for(int i = 0; i < candidates.length; i++) {
        	candidatesLeft.add(i); //Add each index number to the list.
        }
        for(int i = 0; i < setOfBallots.length; i++) { //For each vote in the list of votes.
            Iterator<Integer> reader = setOfBallots[i].getRanking().iterator();//Tells you which candidate is next on this ballot.
            ArrayList<Integer> candsLeft = (ArrayList<Integer>) candidatesLeft.clone();//Copy the list of index numbers. This copied list represents which candidates have not been seen.
            while(reader.hasNext()) {//While there are still rankings on the ballot.
                int rankedCandidate = reader.next(); //Get the next ranked candidate.
                candsLeft.remove(new Integer(rankedCandidate));//Since we now have found this candidate, remove them from the list of candidates that have not been seen on this ballot.
                for(int j = 0; j < candsLeft.size(); j++) {
                    pairwiseTable[rankedCandidate][candsLeft.get(j)]++;//Add one to the count of ballots saying that they prefer the just-found candidate to every candidate that has not been seen on this ballot.
                }
            }
        }
        return pairwiseTable;
    }
    
    //This is the version of the method that doesn't work consistently. 
    public int[][] getThisPairwiseTable(){//Tool for condorcet methods
    	//[i][j] of the output 2D int array is the number of ballots that rank candidate i above candidate j.
        int[][] pairwiseTable = new int[candidates.length][candidates.length];
        ArrayList<Integer> candidatesLeft = new ArrayList<Integer>();//This lists the index numbers of all the candidates.
        for(int i = 0; i < candidates.length; i++) {
        	candidatesLeft.add(i); //Add each index number to the list.
        }
        for(int i = 0; i < this.votes.length; i++) { //For each vote in the list of votes.
            Iterator<Integer> reader = this.votes[i].getRanking().iterator();//Tells you which candidate is next on this ballot.
            ArrayList<Integer> candsLeft = (ArrayList<Integer>) candidatesLeft.clone();//Copy the list of index numbers. This copied list represents which candidates have not been seen.
            while(reader.hasNext()) {//While there are still rankings on the ballot.
                int rankedCandidate = reader.next(); //Get the next ranked candidate.
                candsLeft.remove(new Integer(rankedCandidate));//Since we now have found this candidate, remove them from the list of candidates that have not been seen on this ballot.
                for(int j = 0; j < candsLeft.size(); j++) {
                    pairwiseTable[rankedCandidate][candsLeft.get(j)]++;//Add one to the count of ballots saying that they prefer the just-found candidate to every candidate that has not been seen on this ballot.
                }
            }
        }
        return pairwiseTable;
    }
    
    public int[][] verboseThisPairwiseTable(){//Tool for condorcet methods
    	//[i][j] of the output 2D int array is the number of ballots that rank candidate i above candidate j.
        int[][] pairwiseTable = new int[candidates.length][candidates.length];
        ArrayList<Integer> candidatesLeft = new ArrayList<Integer>();//This lists the index numbers of all the candidates.
        for(int i = 0; i < candidates.length; i++) {
        	candidatesLeft.add(i); //Add each index number to the list.
        }
        for(int i = 0; i < this.votes.length; i++) { //For each vote in the list of votes.
        	System.out.print(i + ": ");
        	Iterator<Integer> reader = this.votes[i].getRanking().iterator();//Tells you which candidate is next on this ballot.
            System.out.print(this.votes[i].toString() + " ");
        	ArrayList<Integer> candsLeft = (ArrayList<Integer>) candidatesLeft.clone();//Copy the list of index numbers. This copied list represents which candidates have not been seen.
            while(reader.hasNext()) {//While there are still rankings on the ballot.
            	int rankedCandidate = reader.next(); //Get the next ranked candidate.
                System.out.print(rankedCandidate + " ");
            	candsLeft.remove(new Integer(rankedCandidate));//Since we now have found this candidate, remove them from the list of candidates that have not been seen on this ballot.
                for(int j = 0; j < candsLeft.size(); j++) {
                    pairwiseTable[rankedCandidate][candsLeft.get(j)]++;//Add one to the count of ballots saying that they prefer the just-found candidate to every candidate that has not been seen on this ballot.
                }
            }
            System.out.println(": " + i);
        }
        return pairwiseTable;
    }
    

    public static int[][] getPreferenceTable(RankedChoiceBallot[] setOfBallots, int numCandidates){ //Tool for positional methods and "iterative" positional methods like Bucklin.
    	//[i][j] of the output 2D int array is the number of ballots ranking candidate i in jth place.
    	//Useful for positional methods.
        int[][] preferenceTable = new int[numCandidates][numCandidates];
        for(int i = 0; i < setOfBallots.length; i++) {
            Iterator<Integer> iterator = setOfBallots[i].getRanking().iterator();
            int rank = 0;
            while(iterator.hasNext()) {
                int candidate = iterator.next();
                preferenceTable[candidate][rank]++;
                rank++;
            }
        }
        return preferenceTable;
    }
    public String[] getCandidates(){
        return candidates;
    }
    public void setCandidates(String[] c) {
        candidates = c;
    }
    public RankedChoiceBallot[] getVotes() {
        return votes;
    }
    public void setVotes(RankedChoiceBallot[] box) {
        votes = box;
    }
}
