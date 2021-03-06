import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;
import java.util.Iterator;
/*
Throw a java.lang.IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.
*/

public class Deque1<Item> implements Iterable<Item> {
  //First Node;
  private Node first;
  //The end of node, i.e. the next of the last node. Always Null.
  private Node last;
  private int n;
  
  private class Node {
    private Item item;
    private Node next;
    private Node previous;
  }
  
  public Deque1(){
    first=new Node();
    last=new Node();
    first.next=last;
    last.previous=first;
    n=0;
  }
  // is the deque empty?
  public boolean isEmpty(){
    return (n==0);
  }
  
  // return the number of items on the deque
  public int size(){
    return n;
  }
  
   // add the item to the front 
  public void addFirst(Item item){
    if(item==null) throw new IllegalArgumentException("Can't add null.");
    if(n==0) {first.item=item;}
    else {
      //Set up a new node
      Node NewFirst=new Node();
      NewFirst.item=item;
      //Conntected forward and backward;
      NewFirst.next=first;
      first.previous=NewFirst;
      //Change the postion of first, always first node;
      first=NewFirst;
    }
    n++;
  }
   
  // add the item to the end
  public void addLast(Item item){
    if(item==null) throw new IllegalArgumentException("Can't add null.");
    //Set last as the last node;
    if(n==0){first.item=item;}
    else{
      last.item=item;
      //Set up new last;
      Node NewLast=new Node();
      //NewLast=null;
      //Connect new last to previous last;
      NewLast.previous=last;
      last.next=NewLast;
      //Change the postion of first, always first node;
      last=NewLast;
    }
    n++;
  }
  
   // remove and return the item from the front
  public Item removeFirst(){
    if(isEmpty()) throw new NoSuchElementException("Already Empty");
    Item item=first.item;
    first=first.next;
    n--;
    return item;
  }               
  
  // remove and return the item from the end
  public Item removeLast(){
    if(isEmpty()) throw new NoSuchElementException("Already Empty");
    Item item = last.previous.item;
    last=last.previous;
    n--;
    return item;
  }
  
  // return an iterator over items in order from front to end
  public Iterator<Item> iterator(){
    return new DequeIterator();  
  }
  
  // unit testing (optional) 
  private class DequeIterator implements Iterator<Item> {
    private Node current=first;
    public boolean hasNext() {return current.next!=null;}
    public Item next(){
      if(!hasNext()) throw new NoSuchElementException("Already Empty");
      Item item = current.item;
      current = current.next; 
      return item;
    }
    public void remove(){throw new UnsupportedOperationException();}
  }
  
  public static void main(String[] args) {
     Deque1<Integer> deque = new Deque1<Integer>();
     //StdOut.print(deque.check());
         deque.addFirst(0);
         deque.removeFirst();
         deque.isEmpty();
         StdOut.println(deque.isEmpty());
         deque.addFirst(3);
         deque.isEmpty() ;
         deque.isEmpty() ;
         deque.removeFirst() ;
         deque.isEmpty() ;
         deque.addFirst(8);
     /*
     while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            deque.addLast(item);
     }
     Iterator<String> iterator = deque.iterator();
     while(iterator.hasNext()) {
       StdOut.println(iterator.next());
     }
    */
    /* 
    Deque<String> deque = new Deque<String>();
     //StdOut.print(deque.check());
    
     while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            deque.addLast(item);
     }
     Iterator<String> iterator = deque.iterator();
     while(iterator.hasNext()) {
       StdOut.println(iterator.next());
     }
     */
     //StdOut.println(deque.isEmpty());
     //deque.removeLast();         
     /*
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            deque.addLast(item);
        }
     */ 
     /*
        while (!deque.isEmpty()) {
            String item =deque.removeLast();
            StdOut.println(item);
            StdOut.println("(" + deque.size() + " left on deque)");
        }
     */   
    }
    //StdOut.print("ok.");
}

