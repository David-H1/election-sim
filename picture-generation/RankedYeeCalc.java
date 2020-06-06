package election.graphic;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import election.ballot.*;
import election.methods.*;

public class RankedYeeCalc{
    public static void main(String[] args) {
      Random positionGen = new Random();
      /*Candidate2D yellow = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.YELLOW, "Yellow");
    	Candidate2D blue = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.BLUE, "Blue");
    	Candidate2D red = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.RED, "Red");
    	Candidate2D green = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.GREEN, "Green");*/
    	Candidate2D yellow = new Candidate2D(17, 54, Color.YELLOW, "Yellow");
    	Candidate2D blue = new Candidate2D(106, 129, Color.BLUE, "Blue");
    	Candidate2D red = new Candidate2D(111, 83, Color.RED, "Red");
    	Candidate2D green = new Candidate2D(55, 185, Color.GREEN, "Green");
    	//Candidate2D[] candidates = {yellow, blue};
      Candidate2D[] candidates = {yellow, blue, red, green};
      RankedChoiceMethod method = new InstantRunoffVoting(null, null);
      System.out.println(Arrays.toString(candidates));
      RankedYeeCalc image = new RankedYeeCalc(220, 220, candidates, 1, 0, method);
    }
    protected int width, height;
    int numCandidates;
    Candidate2D[] candidates;
    int numVoters;
    double stDev;
    RankedChoiceMethod method;
    
    public RankedYeeCalc(int inWidth, int inHeight, Candidate2D[] cands, int voteCount, double standardDeviation, RankedChoiceMethod method) {
    	candidates = cands;
    	numCandidates = candidates.length;
    	numVoters=voteCount;
    	stDev = standardDeviation;
    	width = inWidth;
    	height = inHeight;
    	this.method = method;
    }
    public int[][] getPictureData(){
    	String[] candNames = new String[candidates.length];
    	Random generator = new Random();
    	int[][] pictureData = new int[width][height];
    	for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                RankedChoiceBallot[] box = new RankedChoiceBallot[numVoters];
                double[][] distancesToCandidatesByVoter = new double[numVoters][numCandidates];
                for(int ballot = 0; ballot < numVoters; ballot++) {
                    double voterXPos = x + stDev*generator.nextGaussian();
                    double voterYPos = y + stDev*generator.nextGaussian();
                    double[] distancesToCandidates = new double[numCandidates];
                    for(int i = 0; i < numCandidates; i++) {
                        distancesToCandidates[i] = Math.sqrt(Math.pow(voterXPos-candidates[i].getXPosition(), 2.0) + Math.pow(voterYPos-candidates[i].getYPosition(), 2.0));
                    }
                    distancesToCandidatesByVoter[ballot] = distancesToCandidates;
                    LinkedList<Integer> voterBallot = utilToBallot(distancesToCandidates);
                    RankedChoiceBallot vote = new RankedChoiceBallot(voterBallot, candNames);
                    box[ballot] = vote;
                }
                method.setCandidates(candNames);
                method.setVotes(box);
                pictureData[x][y] = method.getWinner();
            }
    	}
    	return pictureData;
    }
    
    public LinkedList<Integer> utilToBallot(double[] a) {
        double[] sortedA = a.clone();
        Arrays.sort(sortedA);
        HashMap<Double, Integer> utilID = new HashMap<Double, Integer>();
        for(int i = 0; i < a.length; i++) {
            utilID.put(a[i], i);
        }
        LinkedList<Integer> ballot = new LinkedList<Integer>();
        for(int i = 0; i < sortedA.length; i++) {
            ballot.add(utilID.get(sortedA[i]));//Puts in ballot[i] the ID number of the candidate with the ith best value.
        }
        return ballot;
    }
}

