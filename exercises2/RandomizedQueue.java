import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
//uniform(int n)
/*
   Throw a java.lang.IllegalArgumentException if the client calls enqueue() with a null argument.
   Throw a java.util.NoSuchElementException if the client calls either sample() or dequeue() when the randomized queue is empty.
   Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
   Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.
   */

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Item[] array = (Item[]) new Object[1];
    // construct an empty randomized queue
    public RandomizedQueue() {
        n=0;
    }

    private void resize(int Size) {
        Item[] temp = (Item[]) new Object[Size];
        int size_min;
        if (Size<=array.length) {
            size_min=Size;
        }
        else {
            size_min=array.length;
        }

        for (int i=0;i<size_min;i++){
            temp[i] = array[i];
        }
        array = temp;
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return (n==0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the iteAm
    public void enqueue(Item item){
        if(item==null) throw new IllegalArgumentException("Can't add null item.");
        if(n==array.length){
            resize(array.length*2);
        }
        array[n] = item;
        //StdOut.print(n);
        n++;
    }

    // remove and return a random item
    public Item dequeue(){
        //StdOut.print(n);
        if(isEmpty()) throw new NoSuchElementException("Already Empty");
        int index=StdRandom.uniform(n);
        Item item;
        if (n==array.length/4){
            resize(array.length/2);
        }
        item = array[index];
        array[index]=array[n-1];
        array[n-1]=null;
        n--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if(isEmpty()) throw new NoSuchElementException("Already Empty");
        Item item;
        int index=StdRandom.uniform(n);
        //move to the previous to the required node.
        return array[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private RandomizedQueue<Item> SelfCopy () {
        RandomizedQueue<Item> CopyArray  = new RandomizedQueue<Item>();
        if(isEmpty()) throw new java.util.NoSuchElementException ("NullPointer");
        for (int i=0;i<n;i++) {
            CopyArray.enqueue(array[i]);
        }
        return CopyArray;
    }


    private class RandomizedQueueIterator implements Iterator<Item> {
        private RandomizedQueue<Item> CopyArray = SelfCopy ();
        public boolean hasNext() {return !CopyArray.isEmpty();}
        public Item next() {
            if(!hasNext()) throw new NoSuchElementException("Already Empty");
            if(hasNext()){
                Item item = CopyArray.dequeue();
                return item;
            }
            return null;
        }
        public void remove(){throw new UnsupportedOperationException("Can't remove iterator.");}
    }

    // unit testing (optional)
    public static void main(String[] args){
        RandomizedQueue<String> randomizeQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            randomizeQueue.enqueue(item);
        }

        //StdOut.println("Random items are" + randomizeQueue.sample() + ", "+randomizeQueue.sample());
        Iterator<String> iterator = randomizeQueue.iterator();
        //iterator.remove();
        while(iterator.hasNext()) {
            StdOut.println("Iterator:\n"+iterator.next());
        }
        /*
           while (!randomizeQueue.isEmpty()) {
           String item =randomizeQueue.dequeue();
           StdOut.println(item);
           StdOut.println("(" + randomizeQueue.size() + " left on deque)");
           }
           */
    }
}
