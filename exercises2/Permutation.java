import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
  public static void main(String[] args){
    int NumOfDequeue=Integer.parseInt(args[0]);    
    RandomizedQueue<String> randomizeQueue = new RandomizedQueue<String>();
    while (!StdIn.isEmpty()){
      String item = StdIn.readString();
      randomizeQueue.enqueue(item);
    }
    for(int i=0;i<NumOfDequeue;i++){
      StdOut.println(randomizeQueue.dequeue());
    }
  }
}