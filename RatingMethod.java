package deh.methods;

import deh.ballot.ScoreBallot;

public abstract class RatingMethod{
    ScoreBallot[] votes;
    String[] candidates;
    int min;
    int max;
   
    public RatingMethod(ScoreBallot[] ballots, String[] candidateList, int minScore, int maxScore) {
        votes=ballots;
        candidates=candidateList;
        min = minScore;
        max = maxScore;
    }
    public abstract int getWinner();
    public int[] getScoreSum(){
        int[] sumOfScores = new int[candidates.length];
        for(int ballotNum = 0; ballotNum < votes.length; ballotNum++){
            int[] ballotScores = votes[ballotNum].getScores();
            for(int candNum = 0; candNum < candidates.length; candNum++){
                if(ballotScores[candNum] >= min && ballotScores[candNum] <= max){
                    sumOfScores[candNum] += ballotScores[candNum];
                }
            }
        }
        return sumOfScores;
    }
    public double[] getScoreAverage(){
        double[] averageScore = new double[candidates.length];
        int[] numScores = new int[candidates.length];
        for(int ballotNum = 0; ballotNum < votes.length; ballotNum++){
            int[] ballotScores = votes[ballotNum].getScores();
            for(int candNum = 0; candNum < candidates.length; candNum++){
                if(ballotScores[candNum] >= min && ballotScores[candNum] <= max){
                    averageScore[candNum] += ballotScores[candNum];
                    numScores[candNum]++;
                }
            }
        }
        for(int i = 0; i < averageScore.length; i++){
            if(numScores[i] > 0){
                averageScore[i] /= numScores[i];
            }
        }
        return averageScore;
    }
    public int[] getNumScores(){
        int[] numScores = new int[candidates.length];
        for(int ballotNum = 0; ballotNum < votes.length; ballotNum++){
            int[] ballotScores = votes[ballotNum].getScores();
            for(int candNum = 0; candNum < candidates.length; candNum++){
                if(ballotScores[candNum] >= min && ballotScores[candNum] <= max){
                    numScores[candNum]++;
                }
            }
        }
        return numScores;
    }
    public ScoreBallot[] getVotes(){
        return votes;

    }
    public void setVotes(ScoreBallot[] box) {
    	votes = box;
    }
    public String[] getCandidates(){
        return candidates;
    }
    public void setCandidates(String[] cands) {
    	candidates = cands;
    }
    
    public int getMin(){
        return min;
    }
    public int getMax(){
        return max;
    }
}