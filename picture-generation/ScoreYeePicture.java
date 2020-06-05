package election.graphic;

import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Queue;

import election.ballot.*;
import election.methods.*;

//Methods: 1.IRV 2.TopNThreshold 3.Borda 4. Dumb approval 5. Plurality 6. Cycle Free Condorcet 7. Linear Utility Score
public class ScoreYeePicture extends Canvas{
    public static void main(String[] args) {
    	Random positionGen = new Random();
    	
    	Candidate2D yellow = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.YELLOW, "Yellow");
    	Candidate2D blue = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.BLUE, "Blue");
    	Candidate2D red = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.RED, "Red");
    	Candidate2D green = new Candidate2D(positionGen.nextInt(200), positionGen.nextInt(200), Color.GREEN, "Green");
    	/*Candidate2D yellow = new Candidate2D(50, 50, Color.YELLOW, "Yellow");
    	Candidate2D blue = new Candidate2D(50, 150, Color.BLUE, "Blue");
    	Candidate2D red = new Candidate2D(150, 50, Color.RED, "Red");
    	Candidate2D green = new Candidate2D(150, 150, Color.GREEN, "Green");*/

        Candidate2D[] candidates = {yellow, blue, red, green};
        
        RatingMethod method = new ScoreVoting(null, null, 0, 100);//Replace this with some other rated voting method.
        System.out.println(Arrays.toString(candidates));
        ScoreYeePicture image = new ScoreYeePicture("Yee Picture", 220, 220, candidates, 2000, 100, method);
       
        image.paint(image.buffer.getGraphics());
    }
    protected boolean on = true;
    protected int width, height;
    protected Image buffer;
    int numCandidates;
    Candidate2D[] candidates;
    int numVoters;
    double stDev;
    RatingMethod method;
    
    public ScoreYeePicture(String name, int inWidth, int inHeight, Candidate2D[] cands, int voteCount, double standardDeviation, RatingMethod method) {
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
    
    public void paint(Graphics brush) {
    	String[] candNames = new String[candidates.length];
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
        for(int y = 0; y < height-20; y++) {
            for(int x = width-10; x >=0; x--) {
                ScoreBallot[] box = new ScoreBallot[numVoters];
                double[][] distancesToCandidatesByVoter = new double[numVoters][numCandidates];
                for(int ballot = 0; ballot < numVoters; ballot++) {
                    double voterXPos = x + stDev*generator.nextGaussian();
                    double voterYPos = y + stDev*generator.nextGaussian();
                    double[] distancesToCandidates = new double[numCandidates];
                    for(int i = 0; i < numCandidates; i++) {
                        distancesToCandidates[i] = Math.sqrt(Math.pow(voterXPos-candidates[i].getXPosition(), 2.0) + Math.pow(voterYPos-candidates[i].getYPosition(), 2.0));
                    }
                    distancesToCandidatesByVoter[ballot] = distancesToCandidates;
                    int[] voterBallot = utilToScoreBallot(distancesToCandidates, method.getMin(), method.getMax());
                    //int[] voterBallot = utilToCumulBallot(distancesToCandidates, method.getMax());
                    ScoreBallot vote = new ScoreBallot(voterBallot, candNames, method.getMin(), method.getMax());
                    //ScoreBallot vote = new ScoreBallot(voterBallot, candNames, 0, method.getMax());
                    box[ballot] = vote;
                }
                method.setCandidates(candNames);
                method.setVotes(box);
                int winner = method.getWinner();
                brush.setColor(candidates[winner].getColor());
                brush.drawOval(x,y,1,1);
            }
        }
        for(int i = 0; i < candidates.length; i++) {
            Color c = candidates[i].getColor();
            int x = candidates[i].getXPosition();
            int y = candidates[i].getYPosition();
            brush.setColor(Color.BLACK);
            brush.drawOval(x-5, y-5, 10, 10);
            brush.setColor(c);
            brush.fillOval(x-5, y-5, 10, 10);
        }
        Scanner reader = new Scanner(System.in);
        reader.nextLine();
    }
    public int[] utilToScoreBallot(double[] distancesToCandidates, int minScore, int maxScore) {
    	//Scales so that min is the worst candidate, max is the best candidate, and util is 1/sqrt(12000+Distance²)
    	int[] scores = new int[distancesToCandidates.length];
        double[] distances = distancesToCandidates.clone();
        double[] utility = new double[distances.length];
        for(int i = 0; i < distances.length; i++) {
        	utility[i] = 1/Math.sqrt(12000.0+Math.pow(distances[i], 2.0));
        }
        Arrays.sort(distances);
        double bestUtil = 1/Math.sqrt(12000.0+Math.pow(distances[0], 2.0));
        double worstUtil = 1/Math.sqrt(12000.0+Math.pow(distances[distances.length-1], 2.0));
        for(int candID = 0; candID < distancesToCandidates.length; candID++){
                scores[candID] = minScore + (int) Math.round((maxScore - minScore)*(worstUtil-utility[candID])/(worstUtil-bestUtil));
        }
        return scores;
    }
    
    public int[] utilToCumulBallot(double[] distancesToCandidates, int totalPoints) {
    	double[] distances = distancesToCandidates.clone();
        double[] utility = new double[distances.length];
        for(int i = 0; i < distances.length; i++) {
        	utility[i] = 1/Math.sqrt(12000.0+Math.pow(distances[i], 2.0));

        }
        Arrays.sort(distances);
        double bestUtil = 1/Math.sqrt(12000.0+Math.pow(distances[0], 2.0));
        double worstUtil = 1/Math.sqrt(12000.0+Math.pow(distances[distances.length-1], 2.0));
        double utilSum = 0;
        for (int i = 0; i < utility.length; i++) {
        	utility[i] -= worstUtil;
        	utilSum += utility[i];
        }
        int[] points = new int[utility.length];
        int pointsUsed = 0;
        double[] slQuotient = new double[utility.length];
        HashMap<Double, Integer> quotients = new HashMap<Double, Integer>();
        for(int i = 0; i < points.length; i++) {
        	points[i] = (int) (totalPoints*(utility[i])/utilSum);
        	pointsUsed += points[i];
        	slQuotient[i] = utility[i]/(points[i] + 0.5);
        	quotients.put(slQuotient[i], i);
        }
        Arrays.sort(slQuotient);
        LinkedList<Integer> l = new LinkedList<Integer>();
        int current = quotients.get(slQuotient[slQuotient.length-1]);
        l.add(quotients.get(slQuotient[slQuotient.length-2]));
        int place = 3;
        for(; pointsUsed < totalPoints; pointsUsed++) {
        	if(utility[current]/(points[current]+0.5)<utility[l.peek()]/(points[l.peek()]+0.5)) {
        		if(slQuotient[slQuotient.length-place]>utility[current]/(points[current]+0.5)) {
        			ListIterator<Integer> iter = l.listIterator();
        			boolean placed = false;
        			while(!placed & iter.hasNext()) {
        				int cand = iter.next();
        				if(slQuotient[slQuotient.length-place]>=utility[cand]/(points[cand]+0.5)) {
        					iter.previous();
        					iter.add(quotients.get(slQuotient[slQuotient.length-place]));
        					placed=true;
        				}
        			}
        			if(!placed) {
        				iter.add(quotients.get(slQuotient[slQuotient.length-place]));
        			}
        			placed = false;
        			while(!placed & iter.hasNext()) {
        				int cand = iter.next();
        				if(utility[current]/(points[current]+0.5)>=utility[cand]/(points[cand]+0.5)) {
        					iter.previous();
        					iter.add(current);
        					placed=true;
        				}
        			}
        			if(!placed) {
        				iter.add(current);
        			}
        			place++;
        		}
        		else {
        			ListIterator<Integer> iter = l.listIterator();
        			boolean placed = false;
        			while(!placed & iter.hasNext()) {
        				int cand = iter.next();
        				if(utility[current]/(points[current]+0.5)>=utility[cand]/(points[cand]+0.5)) {
        					iter.previous();
        					iter.add(current);
        					placed=true;
        				}
        			}
        			if(!placed) {
        				iter.add(current);
        			}
        		}
        		current = l.pop();
        	}
        	points[current] += 1;
        	
        }
        return points;
    }
}

