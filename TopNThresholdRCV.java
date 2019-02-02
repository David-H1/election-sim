package deh.methods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import deh.ballot.RankedChoiceBallot;

public class TopNThresholdRCV extends RankedChoiceMethod{
  
    public TopNThresholdRCV(RankedChoiceBallot[] ballots, String[] candidateList) {
        super(ballots, candidateList);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getWinner() {
        int numCandidates = super.getCandidates().length;
        RankedChoiceBallot[] voteSet = super.getVotes();
        int numVotes = voteSet.length;
        ArrayList<Integer> candidatesLeft = new ArrayList<Integer>();
        for(int i = 0; i < numCandidates; i++) {
            candidatesLeft.add(i);
        }
        while(candidatesLeft.size() > 1) {
            int numCandidatesLeft = candidatesLeft.size();
            int[][] rankingsReceived = super.getPreferenceTable(voteSet, numCandidates);
            int[] cumulativeTopNRanks = new int[numCandidates];
            int rankingLevel = 1;
            ArrayList<Integer> candidatesReachingThreshold = new ArrayList<Integer>();
            while(candidatesReachingThreshold.isEmpty() && rankingLevel < numCandidatesLeft) {
                int threshold = 1+(numVotes*(rankingLevel)/(rankingLevel+1));
                //System.out.print(rankingLevel + "\t"+ threshold + "\t");
                for(int i = 0; i < numCandidates; i++) {
                    cumulativeTopNRanks[i] += rankingsReceived[i][rankingLevel-1];
                    //System.out.print(cumulativeTopNRanks[i]);
                    if(cumulativeTopNRanks[i] >= threshold) {
                        candidatesReachingThreshold.add(i);
                        //System.out.print("*");
                    }
                    //System.out.print("\t");
                }
                //System.out.println();
                rankingLevel++;
            }
            if(candidatesReachingThreshold.size() > 1) {//Eliminate the candidates that didn't meet the threshold.
                ArrayList<Integer> losers = (ArrayList<Integer>) candidatesLeft.clone();
                losers.removeAll(candidatesReachingThreshold);
                voteSet = super.eliminateCandidatesOnBallots(losers, voteSet);
                candidatesLeft = candidatesReachingThreshold;
                //System.out.println();
            }
            else {
                if(!candidatesReachingThreshold.isEmpty()) {
                    candidatesLeft = candidatesReachingThreshold;
                    //System.out.println();
                }
                else {
                    int randomLoser = candidatesLeft.get(new Random().nextInt(numCandidatesLeft));
                    ArrayList<Integer> loser = new ArrayList<Integer>();
                    loser.add(randomLoser);
                    voteSet = super.eliminateCandidatesOnBallots(loser, voteSet);
                    candidatesLeft.removeAll(loser);
                    //System.out.println();
                }
            }
        }
        return candidatesLeft.get(0);
    }
    
    public String verbose() {
    	String result = "Rank:\tNeed:";
    	for(String candidate : super.getCandidates()) {
    		result += "\t" + candidate.substring(0,Math.min(candidate.length(), 7));
    	}
    	result += "\n";
        int numCandidates = super.getCandidates().length;
        RankedChoiceBallot[] voteSet = super.getVotes();
        int numVotes = voteSet.length;
        ArrayList<Integer> candidatesLeft = new ArrayList<Integer>();
        for(int i = 0; i < numCandidates; i++) {
            candidatesLeft.add(i);
        }
        while(candidatesLeft.size() > 1) {
            int numCandidatesLeft = candidatesLeft.size();
            int[][] rankingsReceived = super.getPreferenceTable(voteSet, numCandidates);
            int[] cumulativeTopNRanks = new int[numCandidates];
            int rankingLevel = 1;
            ArrayList<Integer> candidatesReachingThreshold = new ArrayList<Integer>();
            while(candidatesReachingThreshold.isEmpty() && rankingLevel < numCandidatesLeft) {
                int threshold = 1+(numVotes*(rankingLevel)/(rankingLevel+1));
                result += (rankingLevel + "\t"+ threshold + "\t");
                for(int i = 0; i < numCandidates; i++) {
                    cumulativeTopNRanks[i] += rankingsReceived[i][rankingLevel-1];
                    result += (cumulativeTopNRanks[i]);
                    if(cumulativeTopNRanks[i] >= threshold) {
                        candidatesReachingThreshold.add(i);
                        result += ("*");
                    }
                    result += ("\t");
                }
                result += "\n";
                rankingLevel++;
            }
            if(candidatesReachingThreshold.size() > 1) {//Eliminate the candidates that didn't meet the threshold.
                ArrayList<Integer> losers = (ArrayList<Integer>) candidatesLeft.clone();
                losers.removeAll(candidatesReachingThreshold);
                voteSet = super.eliminateCandidatesOnBallots(losers, voteSet);
                candidatesLeft = candidatesReachingThreshold;
                result += "\n";
            }
            else {
                if(!candidatesReachingThreshold.isEmpty()) {
                    candidatesLeft = candidatesReachingThreshold;
                    result += "\n";
                }
                else {
                    int randomLoser = candidatesLeft.get(new Random().nextInt(numCandidatesLeft));
                    ArrayList<Integer> loser = new ArrayList<Integer>();
                    loser.add(randomLoser);
                    voteSet = super.eliminateCandidatesOnBallots(loser, voteSet);
                    candidatesLeft.removeAll(loser);
                    result += "\n";
                }
            }
        }
        return result;
    }

}
