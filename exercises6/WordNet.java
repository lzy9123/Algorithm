import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.Stack;
import java.util.TreeSet;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;

public class WordNet {
    private Digraph digraph;
    private Map<Integer, String> indexmap;
    private int V = 0;
    private Words words;
    private class Words{
        private Map<String, List<Integer>> wordmap;
        public Words(){
            wordmap = new TreeMap<String, List<Integer>>();
        }

        public void add(String s, Integer i){
            if(wordmap.containsKey(s))
                wordmap.get(s).add(i);
            else{
                List<Integer> list = new ArrayList<Integer> ();
                list.add(i);
                wordmap.put(s, list);
            }
        }

        public Collection<Integer> getIndex(String s){
            if(wordmap.containsKey(s))
                return wordmap.get(s);
            else
                return null;
        }

        public Collection<String> getWords(){
            return wordmap.keySet();
        }

        public int getSize(){
            return wordmap.size();
        }

        public boolean isContains(String s){
            return wordmap.containsKey(s);
        }

    }
        // constructor takes the name of the two input files
        public WordNet(String synsets, String hypernyms){
            if (synsets == null || hypernyms == null)
                throw new IllegalArgumentException("null arguments");
            In in = new In(synsets);
            words = new Words();
            String strLine = "";
            indexmap = new TreeMap<Integer, String>();

            while ((strLine = in.readLine()) != null){
                String[] strsplit = strLine.split(",");
                int i = Integer.parseInt(strsplit[0]);
                indexmap.put(i, strsplit[1]);
                for (String s: strsplit[1].split(" "))
                    words.add(s, i);
            }
            V = words.getSize();
            //StdOut.printf("size of word: %d", V);

            boolean[]isnotroot = new boolean[V];
            List<Integer> roots = new ArrayList<Integer>();
            in = new In(hypernyms);
            digraph = new Digraph(V);
            while ((strLine = in.readLine()) != null){
                if (!roots.contains(V)){
                    roots.add(V);
                }

                String[]strsplit = strLine.split(",");
                int v = Integer.parseInt(strsplit[0]);
                for (int i = 1; i < strsplit.length; i++){
                    digraph.addEdge(v, Integer.parseInt(strsplit[i]));
                }

            }
            /*
            if (!isRootedDAG(digraph, isnotroot)){
                throw new IllegalArgumentException("not rooted DAG");
            }
            */
        }

    private void printline(){
        String nounA = "indirect_lighting";
        String nounB = "Embiotocidae";
        StdOut.println("distance(): " + distance(nounA, nounB));
        StdOut.println("sap(): " + sap(nounA, nounB));
        //int i = 10;
        //String str = WordsToMap.get(i);
        //StdOut.println(this.nouns());
        //StdOut.println(this.isNoun("zymase"));
        //StdOut.println("the size of Map is "+ WordsToMap.size());
        //StdOut.println("the "+ i+" th word is "+ str);
        return;
    }
    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return words.getWords();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if (word == null)
            throw new IllegalArgumentException("isNoun: null");
        return words.isContains(word);
    }

    private Collection<Integer> getIndex(String noun){
        return words.getIndex(noun);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if (isNoun(nounA) && isNoun(nounB)){
            Collection<Integer> index_nounA = words.getIndex(nounA);
            Collection<Integer> index_nounB = words.getIndex(nounB);
            SAP sap = new SAP(digraph);
            return sap.length(index_nounA, index_nounB);
        }
        else{
            throw new IllegalArgumentException("noun not in WordNet");
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("noun not in WordNet");
        Collection<Integer> index_nounA = words.getIndex(nounA);
        Collection<Integer> index_nounB = words.getIndex(nounB);
        SAP sap = new SAP(digraph);
        int index= sap.ancestor(index_nounA, index_nounB);
        return indexmap.get(index);
    }

    // do unit testing of this class
    public static void main(String[] args){
        StdOut.println(args[0]);
        WordNet wordnet = new WordNet(args[0], args[1]);
        wordnet.printline();
    }
}

