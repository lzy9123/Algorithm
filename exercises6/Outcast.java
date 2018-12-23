import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
public class Outcast {
    private WordNet wdnet;
    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        wdnet = wordnet;
    }
    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        String[] nouns_copy = new String[nouns.length];
        System.arraycopy(nouns,0,nouns_copy,0,nouns_copy.length);
        Arrays.sort(nouns_copy);
        Map <String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < nouns_copy.length; i++){
            for(int j = i + 1; j < nouns_copy.length; j++){
                String s = nouns_copy[i].concat(nouns_copy[j]);
                int length = wdnet.distance(nouns_copy[i], nouns_copy[j]);
                map.put(s, length);
                //StdOut.printf("str: %s, length: %d\n", s, length);
            }
        }

        int Max_sum_length = 0;
        String Max_sum_length_str = null;
        for (int i = 0; i < nouns_copy.length; i++){
            int length = 0;
            for(int j = 0; j < nouns_copy.length; j++){
                if (i == j)
                    continue;
                if (i < j){
                    String s = nouns_copy[i].concat(nouns_copy[j]);
                    assert(s!=null);
                    length+=map.get(s);
                }
                else{
                    String s = nouns_copy[j].concat(nouns_copy[i]);
                    assert(s!=null);
                    length+=map.get(s);
                }
            }
            if (length > Max_sum_length){
                Max_sum_length = length;
                Max_sum_length_str = nouns_copy[i];
            }
        }
        return Max_sum_length_str;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
