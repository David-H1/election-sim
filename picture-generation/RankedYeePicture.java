package deh.graphic;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import deh.ballot.*;
import deh.methods.*;

public class RankedYeePicture extends Canvas{
    public static void main(String[] args) {
    	Random positionGen = new Random();
    	
    	Candidate2D yellow = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.YELLOW, "Yellow");
    	Candidate2D blue = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.BLUE, "Blue");
    	Candidate2D red = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.RED, "Red");
    	Candidate2D green = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.GREEN, "Green");
    	//Candidate2D[] candidates = {yellow, blue};
        Candidate2D[] candidates = {yellow, blue, red, green};
        
        CycleFreeCondorcet method = new CycleFreeCondorcet(null, null);
    	
        System.out.println(Arrays.toString(candidates));
        RankedYeePicture image = new RankedYeePicture("Yee Picture", 220, 220, candidates, 500, 100, method);
       
        image.paint(image.buffer.getGraphics());
    }
    protected boolean on = true;
    protected int width, height;
    protected Image buffer;
    int numCandidates;
    Candidate2D[] candidates;
    int numVoters;
    double stDev;
    RankedChoiceMethod method;
    
    public RankedYeePicture(String name, int inWidth, int inHeight, Candidate2D[] cands, int voteCount, double standardDeviation, RankedChoiceMethod method) {
    	candidates = cands;
    	numCandidates = candidates.length;
    	numVoters=voteCount;
    	stDev = standardDeviation;
    	width = inWidth;
    	height = inHeight;
    	this.method = method;
    	// Frame can be read as 'window' here.
    	Frame frame = new Frame(name);
    	frame.add(this);
    	frame.setSize(width,height);
    	frame.setVisible(true);
    	frame.setResizable(false);
    	frame.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {System.exit(0);}
    	});
    	buffer = createImage(width, height);
    }
    public int[][] getPictureData(){
    	String[] candNames = new String[candidates.length];
    	Random generator = new Random();
    	int[][] pictureData = new int[width-9][height-20];
    	for(int y = 0; y < height-20; y++) {
            for(int x = width-10; x >=0; x--) {
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
                int winner = method.getWinner();
                pictureData[x][y] = winner;
            }
    	}
    	return pictureData;
    }
    public void paint(Graphics brush) {
    	String[] candNames = new String[candidates.length];
    	//Draw the circles representing the candidates on the diagram.
        for(int i = 0; i < candidates.length; i++) {
        	candNames[i] = candidates[i].getName();
            Color c = candidates[i].getColor();
            int x = candidates[i].getXPosition();
            int y = candidates[i].getYPosition();
            brush.setColor(Color.BLACK);
            brush.drawOval(x-5, y-5, 10, 10);
            brush.setColor(c);
            brush.fillOval(x-5, y-5, 10, 10);
        }
        
        Random generator = new Random();
        //Simulate an election and color the pixel based on the result.
        for(int y = 0; y < height-20; y++) {
            for(int x = width-10; x >=0; x--) {
                RankedChoiceBallot[] box = new RankedChoiceBallot[numVoters];//Stores the votes of the simulated election.
                //double[][] distancesToCandidatesByVoter = new double[numVoters][numCandidates];//The element indexed to [i][j] is the distance of voter i to candidate j.
                for(int ballot = 0; ballot < numVoters; ballot++) {//Each time through the loop is the generation of another voter.
                    double voterXPos = x + stDev*generator.nextGaussian();//Generate coordinates for this voter.
                    double voterYPos = y + stDev*generator.nextGaussian();
                    double[] distancesToCandidates = new double[numCandidates];
                    for(int i = 0; i < numCandidates; i++) {//Get the distance from this voter's coordinates to each candidate's coordinates.
                        distancesToCandidates[i] = Math.sqrt(Math.pow(voterXPos-candidates[i].getXPosition(), 2.0) + Math.pow(voterYPos-candidates[i].getYPosition(), 2.0));
                    }
                    //distancesToCandidatesByVoter[ballot] = distancesToCandidates;
                    LinkedList<Integer> voterBallot = utilToBallot(distancesToCandidates);//Generate a ranked choice ballot ranking the candidates so the closest is first.
                    RankedChoiceBallot vote = new RankedChoiceBallot(voterBallot, candNames);
                    box[ballot] = vote;
                }
                
                //Use the input election method to compute the winner of the simulated election. 
                method.setCandidates(candNames);
                method.setVotes(box);
                int winner = method.getWinner();
                
                //If there was no winner, then color it black.
                if (winner != -1) {
                	brush.setColor(candidates[winner].getColor());
                    brush.drawOval(x,y,1,1);
                }
                else {//Color the picture based on the color of the winning candidate.
                	brush.setColor(Color.BLACK);
                	brush.drawOval(x,y,1,1);
                }
            }
        }
        //Draw the candidates on the diagram again.
        for(int i = 0; i < candidates.length; i++) {
            Color c = candidates[i].getColor();
            int xPos = candidates[i].getXPosition();
            int yPos = candidates[i].getYPosition();
            brush.setColor(Color.BLACK);
            brush.drawOval(xPos-5, yPos-5, 10, 10);
            brush.setColor(c);
            brush.fillOval(xPos-5, yPos-5, 10, 10);
        }
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

