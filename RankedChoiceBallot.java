package deh.ballot;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * Contains the votes that will be counted by a subclass of RankedChoiceMethod
 */

public class RankedChoiceBallot implements Cloneable{
    LinkedList<Integer> ranking;//Ordered by preference, candidates are referred to by their index number in candidates.
    String[] candidates;//Corresponds to the names of the candidates.
    public RankedChoiceBallot(LinkedList<Integer> vote, String[] choices) {
        ranking=vote;
        candidates=choices;
    }
    public LinkedList<Integer> getRanking(){
        return ranking;
    }
    public String[] getCandidates(){
        return candidates;
    }
    public String toString() {
        String out = "";
        Iterator iterate = ranking.iterator();
        while(iterate.hasNext()) {
            out += iterate.next().toString();
            out += ">";
        }
        return out.substring(0,out.length()-1);
    }
    
    @Override
    public RankedChoiceBallot clone() {
		return new RankedChoiceBallot((LinkedList<Integer>) ranking.clone(), candidates.clone());
    	
    }
}