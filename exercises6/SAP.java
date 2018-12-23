import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.TreeMap;
import java.util.Stack;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Deque;
import java.lang.IllegalArgumentException;

public class SAP {
    private final Digraph G_C;
    private int[]last_node;
    private int range_vertex;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(final Digraph G){
        range_vertex = G.V();
        if(G == null)
            throw new IllegalArgumentException("null digraph");
        G_C = new Digraph(G);
    }


    private class BFS {
        private int root;
        private Deque<Integer> path = new ArrayDeque<Integer>();
        private int[]length_array = new int[SAP.this.range_vertex];
        private boolean[]is_checked = new boolean[SAP.this.range_vertex];
        private Deque<Integer> points = new ArrayDeque<Integer>();
        private Deque<Integer> add_points = new ArrayDeque<Integer>();
        public BFS(int i){
            root = i;
            length_array[i] = 0;
            is_checked[i] = true;
            path.addLast(i);
            points.addLast(i);
            add_points.addLast(i);
        }

        public int strlen(){
            return length_array.length;
        }

        public void one_search(){
            add_points.clear();
            int i = path.pollFirst();
            for (int j :G_C.adj(i)){
                if (!is_checked[j]){
                    is_checked[j] = true;
                    length_array[j] = length_array[i]+1;
                    path.addLast(j);
                    points.addLast(j);
                    add_points.addLast(j);
                }
            }
        }

        public boolean isEnd(){
            return path.isEmpty();
        }

        public Iterable<Integer> get_all_points(){
            return points;
        }

        public int get_length(int i){
            if (is_checked[i])
                return length_array[i];
            else
                return -1;
        }

        public Iterable<Integer> get_add_points(){
            return add_points;
        }

        public int[] get_connect(BFS that){
            int length = -1;
            int ancestor = -1;
            for (Integer i: that.get_all_points()){
                if (points.contains(i)){
                    int newlength = length_array[i] + that.get_length(i);
                    if(length == -1 || newlength  < length){
                        length = newlength;
                        ancestor = i;
                    }
                }
            }
            int []result = {length, ancestor};
            return result;
        }
        public void get_all_points_string(){
            //StdOut.print("points:\n");
            for(Integer i:points){
               StdOut.print(i+"; ");
            }
            //StdOut.print("\n");
        }
    }


    private int[]BFS(int v, int w){
        //initialize
        BFS v_path = new BFS(v);
        BFS w_path = new BFS(w);
        int minlength = -1;
        int ancestor = -1;

        while (!v_path.isEnd() || !w_path.isEnd()){
            if(!v_path.isEnd())
                v_path.one_search();
            if(!w_path.isEnd())
                w_path.one_search();
            int[] result = v_path.get_connect(w_path);
            if (result[0] != -1)
                if (minlength == -1 || result[0] < minlength){
                    minlength = result[0];
                    ancestor = result[1];
                }

            //v_path.get_all_points_string();
            //w_path.get_all_points_string();
        }
        int [] result = {minlength, ancestor};
        return result;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        if(v<0 || w<0 || v>= range_vertex || w >= range_vertex)
            throw new IllegalArgumentException("vertex out of range");
        if (v == w)
            return 0;
        return BFS(v,w)[0];
    }

    private boolean isContains(int v, int w){
        for(int i : G_C.adj(v)){
            if (i == w)
                return true;
        }
        return false;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        if(v<0 || w<0 || v>= range_vertex || w >= range_vertex)
            throw new IllegalArgumentException("vertex out of range");
        if (v == w)
            return v;
        return BFS(v,w)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null)
            throw new IllegalArgumentException("null iterable collection");
        int length = -1;
        for (Integer i:v){
            for (Integer j: w){
                if(i == null || j == null || i<0 || j<0 || i>= range_vertex || j >= range_vertex)
                    throw new IllegalArgumentException("vertex out of range");
                int l = length(i,j);
                if (length == -1 || l  < length)
                    length = l;
            }
        }
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null)
            throw new IllegalArgumentException("null iterable collection");
        int length = -1;
        int ancestor = -1;
        for (Integer i:v){
            for (Integer j: w){
                if(i == null || j == null || i<0 || j<0 || i>= range_vertex || j >= range_vertex)
                    throw new IllegalArgumentException("vertex out of range");
                int l = length(i, j);
                if (length == -1 || l < length){
                    ancestor = ancestor(i, j);
                    length = l;
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        /*
        int v = 6;
        int w = 4;
        int length   = sap.length(v, w);

        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        */
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);

            StdOut.printf("length = %d\n", length);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

