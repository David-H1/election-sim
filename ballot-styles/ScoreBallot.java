package deh.ballot;

public class ScoreBallot{
    int[] scores;//The order of scores corresponds to the order of candidates.
    String[] candidates;
    int min;
    int max;
    
    public ScoreBallot(int[] vote, String[] candNames, int minScore, int maxScore) {
        scores = vote;
        candidates = candNames;
        min = minScore;
        max = maxScore;
    }
   
    public int[] getScores(){
        return scores;
    }
    public String[] getCandidates(){
        return candidates;
    }
    public int getMin(){
        return min;
    }
    public int getMax(){
        return max;
    }
    public String toString() {
        String out = "";
        for(int cand = 0; cand < candidates.length; cand++) {
            out += candidates[cand] + ":";
            if(scores[cand] >= min && scores[cand] <= max){
                out += scores[cand];
            }
            else{
                out += "X";
            }
            out += "\n";
        }
        return out;
    }
}
