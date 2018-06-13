import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Iterator;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
public class Solver {
    private int move_solution = 0;
    private SearchNode current1 = null;
    private boolean solvable = true;
    private MinPQ<SearchNode> minpq1 = new MinPQ<SearchNode>();
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial == null)
            throw new IllegalArgumentException("initial null");
        //MinPQ<SearchNode> minpq2 = new MinPQ<SearchNode>();
            //int move2 = 0;
            //SearchNode current2 = new SearchNode(initial.twin(), move2, null);
            //StdOut.print(current.getBoard().toString());
            minpq1.insert(new SearchNode(initial, null));

            //minpq2.insert(current2);
            //current = current1.getBoard();
            while (true){
                current1 = minpq1.delMin();
                if (current1.getBoard().isGoal()){
                    break;
                }
                if (current1.getBoard().twin().isGoal()){
                    solvable = false;
                    break;
                }
                addMinPQ(current1);
            }
    }

    private void addMinPQ(SearchNode p){
        for (Board b1:p.getBoard().neighbors()){
            if (p.getLast() == null || !b1.equals(p.getLast().getBoard())){
                minpq1.insert(new SearchNode(b1, p));
            }
        }
    }


    private class SearchNode implements Comparable<SearchNode>{
        private int move;
        private Board board;
        private SearchNode last;
        public SearchNode(Board b, SearchNode pre){
            board = b;
            if (pre == null){
                last = null;
                move = 0;
            }
            else {
                last = pre;
                move = pre.getMove()+1;
            }
        }
        public Board getBoard(){
            return board;
        }
        public int getMove(){
            return move;
        }
        public SearchNode getLast(){
            return last;
        }
        public int compareTo (SearchNode that){
            if (board.manhattan()+move < that.getBoard().manhattan()+ that.getMove())
                return -1;
            else if (board.manhattan()+move > that.getBoard().manhattan()+ that.getMove())
                return 1;
            else
                return 0;
        }

    }


    // is the initial board solvable?
    public boolean isSolvable(){
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
         if(solvable)
            return current1.getMove();
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if (!solvable)
            return null;
        else{
            Stack<Board> stack = new Stack<Board>();
            SearchNode _current = current1;
            while (_current!=null){
                stack.push(_current.getBoard());
                _current= _current.getLast();
            }

            return stack;
        }
    }


    // solve a slider puzzle (given below)
    public static void main(String[] args){

    }


}
