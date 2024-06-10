import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import org.javatuples.Triplet;


public class WordFreq {
    // You are allowed to add private methods, or private members but you cannot
    // alter the return or parameter type of either the following public
    // methods.

    Map<Integer, Hashtable<String, Integer>> map;

    /**
     * @param dict
     *             dict is an iterator of triplets which stores
     *             String = word
     *             Integer0 = year
     *             Integer1 = frequency
     */
    public WordFreq(Iterator<Triplet<String, Integer, Integer>> dictionary) {
       
        map = new HashMap<Integer, Hashtable<String, Integer>>();
  
        while (dictionary.hasNext()) {
            Triplet<String, Integer, Integer> entry = dictionary.next();
            String word = entry.getValue0();
            int year = entry.getValue1();
            int freq = entry.getValue2();

            if (!map.containsKey(year)) {
                Hashtable<String, Integer> table = new Hashtable<String, Integer>();
                table.put(word, freq);
                map.put(year, table);
            } else {
                map.get(year).put(word, freq);
            }
            
        }


    }

    /**
     * @param query
     * @param startYear
     * @return Set<Pair<year, frequency>>
     */
    public Set<Pair<Integer, Integer>> wordFreq(String query, int startYear) {
        Set<Pair<Integer,Integer>> ret =  new HashSet<Pair<Integer,Integer>>();
        for (int i = startYear; i <= 2008; ++i) {
            Integer freq = map.get(i).get(query);
            if (freq != null) {
                ret.add(new Pair<Integer,Integer>(i, freq));
            }
            
        }
        return ret;
    }
}
