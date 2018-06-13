import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.lang.IllegalArgumentException;

public class Board {
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    private int [][]_blocks;
    private int _hamming=0;
    private int _manhattan=0;
    private int N;
    private int blank_i, blank_j;
    private int mov = -1;
    public Board(int[][] blocks){
        N = blocks.length;
        _blocks= new int[N][N];
        for (int i =0; i<N; i++){
            for (int j = 0; j<N; j++){
                _blocks[i][j]=blocks[i][j];
            }
        }
        if (mov == -1){
            initial();
            mov ++;
        }
    }

    private void initial(){
        for (int i =0; i<N; i++){
            for (int j = 0; j<N; j++){
                if (_blocks[i][j] != 0){
                    _hamming += getOneHamming(_blocks,i,j);
                    _manhattan+= getOneManhatan(_blocks,i,j);
                    //StdOut.print(" "+getOneManhatan(i,j)+" ");
                }
                else {
                    blank_i = i;
                    blank_j = j;
                }
            }
        }
    }



    private enum direction{
        LEFT,RIGHT,UP,DOWN
    }

    private Board getNeighbor( direction d ){
        int i,j;
        switch (d){
            case UP:
                i = blank_i-1;
                j = blank_j;
                break;
            case DOWN:
                i = blank_i+1;
                j = blank_j;
                break;
            case LEFT:
                i = blank_i;
                j = blank_j-1;
                break;
            case RIGHT:
                i = blank_i;
                j = blank_j+1;
                break;
            default:
                throw new IllegalArgumentException("no direction");
        }
        if (isOutRange(i,j))
            return null;
        else {
            int [][] _blocks_c = getCopy();
            int _hamming_0 = getOneHamming(_blocks_c,i, j);
            int _manhattan_0 = getOneManhatan (_blocks_c,i, j);
            int temp = _blocks_c[i][j];
            _blocks_c[i][j] = 0;
            _blocks_c[blank_i][blank_j] = temp;
            int _hamming_1 = getOneHamming(_blocks_c,blank_i, blank_j);
            int _manhattan_1 = getOneManhatan(_blocks_c,blank_i, blank_j);
            Board getNeighbor = new Board(_blocks_c);
            getNeighbor.mov = mov+1;
            getNeighbor._hamming = _hamming + _hamming_1 - _hamming_0;
            getNeighbor._manhattan =_manhattan + _manhattan_1 - _manhattan_0;
            getNeighbor.blank_i = i;
            getNeighbor.blank_j = j;
            return getNeighbor;
        }

    }

    private int getOneHamming (int[][] thisblock, int i, int j){
        int result = (thisblock[i][j] == N*i + j +1)? 0:1;
        return result;
    }

    private int getOneManhatan (int[][] thisblock,int i, int j){
            int _i = (thisblock[i][j]-1)/N;
            int _j = (thisblock[i][j]-1) % N;
            int result = Math.abs(_i-i)+Math.abs(_j-j);
            return result;
    }

    private boolean isOutRange(int i, int j){
        if (i<0 || j<0 || i>N-1 || j>N-1){
            return true;
        }
        else
            return false;
    }

    // board dimension n
    public int dimension(){
        return _blocks.length;
    }

    // number of blocks out of place
    public int hamming(){
        return _hamming;
    }

     // sum of Manhattan distances between blocks and goal
    public int manhattan(){
        return _manhattan;
    }

    // is this board the goal board?
    public boolean isGoal(){
        boolean result = _hamming==0 || _manhattan==0;
        if (result)
            assert(_hamming==_manhattan);
        return result;
    }

    private int [][] getCopy(){
        int [][] copy = new int [N][N];
        for (int i =0; i<N; i++){
            for (int j = 0; j<N; j++){
                copy[i][j] = _blocks[i][j];
            }
        }
        return copy;

    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin(){
        //swap 1 to 2
        int i1,i2,j1,j2;
        i1 = i2 = j1 = j2 =-1;
        int [][]_blocks_tw = getCopy();
        for (int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                if(_blocks_tw[i][j] == 1){
                    i1 = i;
                    j1 = j;
                }
                if (_blocks_tw[i][j] == 2){
                    i2 = i;
                    j2 = j;
                }
            }
        }
        if (i1 < 0|| i2<0 || j1<0 || j2<0)
            throw new IllegalArgumentException("twin wrong");

        int temp = _blocks_tw[i1][j1];
        _blocks_tw[i1][j1] = _blocks_tw[i2][j2];
        _blocks_tw[i2][j2] = temp;
        return new Board(_blocks_tw);
    }

    // does this board equal y?
    // some question
    public boolean equals(Object y){
        if (y == null) return false;
        else if (y == this) return true;
        else if (this.getClass() != y.getClass()) return false;
        else {
            Board _y = (Board) y;
            if (_y.dimension() != this.dimension()) return false;
            else{
                boolean result = true;
                int [][] _blocks_y = _y._blocks;
Label1:
                for (int i = 0; i < N; i++){
                    for (int j = 0; j<N; j++){
                        if (_blocks[i][j] != _blocks_y[i][j]){
                            result = false;
                            break Label1;
                        }
                    }
                }
                return result;
            }
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        Queue<Board> queue = new Queue<Board> ();
        Board b;
        direction [] directions = {direction.LEFT, direction.RIGHT, direction.UP, direction.DOWN};
        for (direction d: directions){
            b = Board.this.getNeighbor(d);
            if(b != null)
                queue.enqueue(b);
            //Board b = Board.this.getNeighbor(direction.RIGHT);
            //Board b = new Board(_blocks_n);
            //queue.enqueue(new Board(_blocks_n));
            //StdOut.print("inner size :" + queue.size());
        }
        return  queue;
    }


    // string representation of this board (in the output format specified below)
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", _blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args){
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        //StdOut.print("hamming: "+initial.hamming()+" manhattan: "+initial.manhattan()+"\n");
        //StdOut.print(initial.mov);
        StdOut.println(initial.toString());




        //Board i2 = initial.getNeighbor(direction.RIGHT);
        //StdOut.print("hamming: "+i2.hamming()+" manhattan: "+i2.manhattan()+"\n");
        //StdOut.println(i2.toString());

        //StdOut.print("\n");


        Queue<Board> queue = (Queue<Board>) initial.neighbors();


        while (!queue.isEmpty()){
            StdOut.println("Iterator:\n" + queue.dequeue().toString());
        }

        Board e1 = initial.getNeighbor(direction.LEFT);
        Board e2 = e1.getNeighbor(direction.RIGHT);

        StdOut.print("\n e2: \n" + e2.toString());
        boolean a = e2.equals(initial);
        StdOut.println("True: "+ e2.equals(initial));
    }
}
