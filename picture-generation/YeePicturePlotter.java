package election.graphic;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import election.methods.*;

/**
 * Plots Yee Pictures using precalculated data.
 * It's better to use this class to draw Yee Pictures and calculate data
 * using the Calc classes than to draw Yee Pictures from RankedYeePicture
 * or ScoreYeePicture due to some issues with the graphics and threads.
 */
public class YeePicturePlotter extends Canvas{
	public static void main(String[] args) {
    	Candidate2D yellow = new Candidate2D(17, 54, Color.YELLOW, "Yellow");
    	Candidate2D blue = new Candidate2D(106, 129, Color.BLUE, "Blue");
    	Candidate2D red = new Candidate2D(111, 83, Color.RED, "Red");
    	Candidate2D green = new Candidate2D(55, 185, Color.GREEN, "Green");
		/*Candidate2D yellow = new Candidate2D(50, 150, Color.YELLOW, "Yellow");
    	Candidate2D blue = new Candidate2D(50, 50, Color.BLUE, "Blue");
    	Candidate2D red = new Candidate2D(150, 50, Color.RED, "Red");
    	Candidate2D green = new Candidate2D(150, 150, Color.GREEN, "Green");*/
    	//Candidate2D[] candidates = {yellow, blue};
      Candidate2D[] candidates = {yellow, blue, red, green};
        
      RankedChoiceMethod rankedMethod = new InstantRunoffVoting(null, null);
      RatingMethod scoreMethod = new StarVoting(null, null);
      System.out.println(Arrays.toString(candidates));
        
      RankedYeeCalc calc = new RankedYeeCalc(220, 220, candidates, 1000, 100, rankedMethod);
      int[][] data = calc.getPictureData();
      YeePicturePlotter image = new YeePicturePlotter("Yee Picture", data, candidates);
      image.paint(image.buffer.getGraphics());
      int[][] data1 = dataSmoothifier(data, candidates.length);
      YeePicturePlotter image1 = new YeePicturePlotter("Smooth Picture", data1, candidates);
      image1.paint(image1.buffer.getGraphics());
        
      ScoreYeeCalc scoreCalc = new ScoreYeeCalc(220, 220, candidates, 1000, 100, scoreMethod);
      int[][] scoreData = scoreCalc.getPictureData();
      YeePicturePlotter scoreImage = new YeePicturePlotter("Yee Picture", scoreData, candidates);
      scoreImage.paint(scoreImage.buffer.getGraphics());
      int[][] smoothScoreData = dataSmoothifier(scoreData, candidates.length);
      YeePicturePlotter scoreImage1 = new YeePicturePlotter("Smooth Picture", smoothScoreData, candidates);
      scoreImage1.paint(scoreImage1.buffer.getGraphics());
	}
    protected boolean on = true;
    protected int width, height;
    protected Image buffer;
    int numCandidates;
    Candidate2D[] candidates;
    int[][] data;
    public YeePicturePlotter(String name, int[][] pictureData, Candidate2D[] cands) {
    	candidates = cands;
    	numCandidates = candidates.length;
    	width = pictureData.length;
    	height = pictureData[0].length;
    	data = pictureData;
    	
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
        
        //Color the pixel based on the input data.
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int winner = data[x][y];
                
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
    
    public static int[][] dataSmoothifier(int[][] data, int numCandidates){
    	//Hides variation by changing pixels that disagree with their neighbors. 
    	int[][] smooth = new int[data.length][data[0].length];
    	Random gen = new Random();
    	for(int i = 0; i < data.length; i++) {
    		for(int j = 0; j < data[0].length; j++) {
        		int[] valScore = new int[numCandidates];
        		valScore[data[i][j]] += 15;
        		try {valScore[data[i][j+1]] += 11;} catch(ArrayIndexOutOfBoundsException e) {}
        		try {valScore[data[i][j-1]] += 11;} catch(ArrayIndexOutOfBoundsException e) {}
        		try {valScore[data[i-1][j]] += 11;} catch(ArrayIndexOutOfBoundsException e) {}
        		try {valScore[data[i+1][j]] += 11;} catch(ArrayIndexOutOfBoundsException e) {}
        		try {valScore[data[i-1][j-1]] += 10;} catch(ArrayIndexOutOfBoundsException e) {}
        		try {valScore[data[i-1][j+1]] += 10;} catch(ArrayIndexOutOfBoundsException e) {}
        		try {valScore[data[i+1][j-1]] += 10;} catch(ArrayIndexOutOfBoundsException e) {}
        		try {valScore[data[i+1][j+1]] += 10;} catch(ArrayIndexOutOfBoundsException e) {}
        		int highScore = 14;
        		int holder = 0;
        		double tiebreak = 0;
        		for(int val = 0; val < numCandidates; val++) {
        			if(valScore[val] > highScore) {
        				tiebreak = gen.nextDouble();
        				holder = val;
        				highScore = valScore[val];
        			}
        			else if (valScore[val] == highScore) {
        				double tiebreak1 = gen.nextDouble();
        				if (tiebreak1 > tiebreak) {
        					tiebreak = tiebreak1;
        					holder = val;
            				highScore = valScore[val];
        				}
        			}
        		}
        		smooth[i][j] = holder;
    		}
    	}
    	return smooth;
    }
}
