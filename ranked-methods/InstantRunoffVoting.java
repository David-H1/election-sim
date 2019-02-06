package dh.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import dh.ballot.RankedChoiceBallot;

public class InstantRunoffVoting extends RankedChoiceMethod{
    public InstantRunoffVoting(RankedChoiceBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getWinner() {
    	Random gen = new Random();
        int numCandidates = super.getCandidates().length;
        RankedChoiceBallot[] voteSet = super.getVotes();
        int winner = -1;
        ArrayList<Integer> candidatesLeft = new ArrayList<Integer>();
        for(int i = 0; i < numCandidates; i++) {
            candidatesLeft.add(i);
        }
        int numVotes = voteSet.length;
        int threshold = 1 + numVotes/2;
        boolean metThreshold = false;
        while(!metThreshold) {
        	double tiebreakPriority = gen.nextDouble();
            int[] voteCount = new int[numCandidates];
            int exhaustedBallots = 0;
            for(RankedChoiceBallot vote : voteSet) {
                if(!vote.getRanking().isEmpty()) {
                	int cand = vote.getRanking().getFirst();
                	voteCount[cand]++;
                }
                else {
                	exhaustedBallots++;
                }
            }
            threshold = 1 + (numVotes-exhaustedBallots)/2;
            //System.out.println(Arrays.toString(voteCount));
            int smallestVoteCount=Integer.MAX_VALUE; int lastPlace = 0;
            for(int i = 0; i < candidatesLeft.size(); i++) {
                int candID = candidatesLeft.get(i);
                //System.out.print(candID);
                if(voteCount[candID] >= threshold) {
                    winner=candID;
                    metThreshold=true;
                    break;
                }
                else {
                    if(voteCount[candID] < smallestVoteCount) {
                        smallestVoteCount=voteCount[candID];
                        lastPlace = candID;
                        tiebreakPriority = gen.nextDouble();
                    }
                    if(voteCount[candID] == smallestVoteCount) {
                    	double secondTiebreakPriority = gen.nextDouble();
                    	if(tiebreakPriority >= secondTiebreakPriority) {
                    		tiebreakPriority = secondTiebreakPriority;
                    		lastPlace = candID;
                    	}
                    }
                }
            }
            if(candidatesLeft.size()==2 && !metThreshold){
                if(gen.nextBoolean()){
                    winner = candidatesLeft.get(0);
                }
                else{
                    winner = candidatesLeft.get(1);
                }
                metThreshold = true;
            }
            //System.out.print(lastPlace);
            ArrayList<Integer> loser = new ArrayList<Integer>();
            loser.add(lastPlace);
            voteSet = super.eliminateCandidatesOnBallots(loser, voteSet);
            candidatesLeft.removeAll(loser);
        }
        return winner;
    }
    
    public String verbose() {
    	String result = "";
    	Random gen = new Random();
        int numCandidates = super.getCandidates().length;
        RankedChoiceBallot[] voteSet = super.getVotes();
        int winner = -1;
        ArrayList<Integer> candidatesLeft = new ArrayList<Integer>();
        for(int i = 0; i < numCandidates; i++) {
            candidatesLeft.add(i);
        }
        int numVotes = voteSet.length;
        int threshold = 1 + numVotes/2;
        boolean metThreshold = false;
        while(!metThreshold) {
        	double tiebreakPriority = gen.nextDouble();
            int[] voteCount = new int[numCandidates];
            int exhaustedBallots = 0;
            for(RankedChoiceBallot vote : voteSet) {
                if(!vote.getRanking().isEmpty()) {
                	int cand = vote.getRanking().getFirst();
                	voteCount[cand]++;
                }
                else {
                	exhaustedBallots++;
                }
            }
            threshold = 1 + (numVotes-exhaustedBallots)/2;
            result += Arrays.toString(voteCount);
            int smallestVoteCount=Integer.MAX_VALUE; int lastPlace = 0;
            for(int i = 0; i < candidatesLeft.size(); i++) {
                int candID = candidatesLeft.get(i);
                //System.out.print(candID);
                if(voteCount[candID] >= threshold) {
                    winner=candID;
                    metThreshold=true;
                    break;
                }
                else {
                    if(voteCount[candID] < smallestVoteCount) {
                        smallestVoteCount=voteCount[candID];
                        lastPlace = candID;
                        tiebreakPriority = gen.nextDouble();
                    }
                    if(voteCount[candID] == smallestVoteCount) {
                    	double secondTiebreakPriority = gen.nextDouble();
                    	if(tiebreakPriority >= secondTiebreakPriority) {
                    		tiebreakPriority = secondTiebreakPriority;
                    		lastPlace = candID;
                    	}
                    }
                }
            }
            if(candidatesLeft.size()==2 && !metThreshold){
                if(gen.nextBoolean()){
                    winner = candidatesLeft.get(0);
                }
                else{
                    winner = candidatesLeft.get(1);
                }
                metThreshold = true;
            }
            result += "Eliminate:" + lastPlace + "\n";
            ArrayList<Integer> loser = new ArrayList<Integer>();
            loser.add(lastPlace);
            voteSet = super.eliminateCandidatesOnBallots(loser, voteSet);
            candidatesLeft.removeAll(loser);
        }
        result += "Winner:" + winner;
        return result;
    }

}
