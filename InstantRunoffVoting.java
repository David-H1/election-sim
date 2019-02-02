package deh.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import deh.ballot.RankedChoiceBallot;

public class InstantRunoffVoting extends RankedChoiceMethod{
    public static void main (String[] args) {
        boolean going = true;
        int tries = 0;
        String[] candidates = {"A. Tony", "Y. Grue", "X. Dick", "W. Balls", "V. Total", "U. Moron", "Z. Hugh"};
        RankedChoiceBallot[] chaos = new RankedChoiceBallot[52];
        LinkedList<Integer> type1 = new LinkedList<Integer>();//A (1x)
        type1.add(0);
        LinkedList<Integer> type2 = new LinkedList<Integer>();//Y>A (1x)
        type2.add(1); type2.add(0);
        LinkedList<Integer> type3 = new LinkedList<Integer>();//X>Y>A (2x)
        type3.add(2); type3.add(1); type3.add(0);
        LinkedList<Integer> type4 = new LinkedList<Integer>();//W>X>Y>A (4x)
        type4.add(3); type4.add(2); type4.add(1); type4.add(0);
        LinkedList<Integer> type5 = new LinkedList<Integer>();//V>W>X>Y>A (8x)
        type5.add(4); type5.add(3); type5.add(2); type5.add(1); type5.add(0);
        LinkedList<Integer> type6 = new LinkedList<Integer>();//U>V>W>X>Y>A (16x)
        type6.add(5); type6.add(4); type6.add(3); type6.add(2); type6.add(1); type6.add(0);
        LinkedList<Integer> type7 = new LinkedList<Integer>();//Z (20x)
        type7.add(6);
        int i = 0;
        for(; i < 1; i++) {
        	RankedChoiceBallot vote = new RankedChoiceBallot((LinkedList<Integer>) type1.clone(), candidates);
        	chaos[i] = vote;
        }
        for(; i < 1+1; i++) {
        	RankedChoiceBallot vote = new RankedChoiceBallot((LinkedList<Integer>) type2.clone(), candidates);
        	chaos[i] = vote;
        }
        for(; i < 1+1+2; i++) {
        	RankedChoiceBallot vote = new RankedChoiceBallot((LinkedList<Integer>) type3.clone(), candidates);
        	chaos[i] = vote;
        }
        for(; i < 1+1+2+4; i++) {
        	RankedChoiceBallot vote = new RankedChoiceBallot((LinkedList<Integer>) type4.clone(), candidates);
        	chaos[i] = vote;
        }
        for(; i < 1+1+2+4+8; i++) {
        	RankedChoiceBallot vote = new RankedChoiceBallot((LinkedList<Integer>) type5.clone(), candidates);
        	chaos[i] = vote;
        }
        for(; i < 1+1+2+4+8+16; i++) {
        	RankedChoiceBallot vote = new RankedChoiceBallot((LinkedList<Integer>) type6.clone(), candidates);
        	chaos[i] = vote;
        }
        for(; i < 1+1+2+4+8+16+20; i++) {
        	RankedChoiceBallot vote = new RankedChoiceBallot((LinkedList<Integer>) type7.clone(), candidates);
        	chaos[i] = vote;
        }
        InstantRunoffVoting chaosScenario2 = new InstantRunoffVoting(chaos, candidates);
    	while(going) {
    	tries++;
        String chaosResult = chaosScenario2.verbose();
        System.out.println(chaosResult);
        if(chaosResult.contains("Winner:0")) {
        	going=false;
        }
        }
    	System.out.print("\n"+ tries);
    }
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