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

public class Deque<Item> implements Iterable<Item> {
  //First Node;
  private Node first;
  //The end of node, i.e. the next of the last node. Always Null.
  private Node last;
  private int n;
  
  private class Node {
    private Item item;
    private Node next=null;
    private Node previous=null;
  }
  
  public Deque(){
    first=null;
    last=null;
    //first.next=last;
    //last.previous=first;
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
    if(isEmpty()) {first=new Node();first.item=item;last=first;}
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
    if(isEmpty()){first=new Node();first.item=item;last=first;}
    else{
       //Set up new last;
      Node NewLast=new Node();
      NewLast.item = item;
      //NewLast=null;
      //Connect new last to previous last;
      last.next=NewLast;
      NewLast.previous=last;
      //Change the postion of first, always first node;
      last=NewLast;
    }
    n++;
  }
  
   // remove and return the item from the front
  public Item removeFirst(){
    if(isEmpty()) throw new NoSuchElementException("Already Empty");
    if(n>1){
      Item item=first.item;
      first=first.next;
      first.previous=null;
      n--;
      return item;
    }
    else if(n==1){
      Item item=first.item;
      first=null;
      last=null;
      n--;
      return item;
    }
    else return null;  
  }               
  
  // remove and return the item from the end
  public Item removeLast(){
    if(isEmpty()) throw new NoSuchElementException("Already Empty");
    if(n>1){
        Item item = last.item;
        last=last.previous;
        last.next=null;
        n--;
        return item;
    }
    else if(n==1){
      Item item=first.item;
      first=null;
      last=null;
      n--;
      return item;
    }
    else return null;
  }
  
  // return an iterator over items in order from front to end
  public Iterator<Item> iterator(){
    return new DequeIterator();  
  }
  
  // unit testing (optional) 
  private class DequeIterator implements Iterator<Item> {
    private Node current = first;
    private int size = n;
    public boolean hasNext() {return size!=0;}
    public Item next(){
      if(!hasNext()) throw new NoSuchElementException("Already Empty");
      if(hasNext()){
      Item item = current.item;
      current = current.next; 
      size--;
      return item;
      }
      return null;
    }
    public void remove(){throw new UnsupportedOperationException();}
  }
  
  
  public static void main(String[] args) {        

    //Deque<String> deque = new Deque<String>();
     //StdOut.print(deque.check());
     /*
     while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            deque.addFirst(item);
     }
     */
     Deque<Integer> deque = new Deque<Integer>();
   

     deque.addLast(6);
     deque.addFirst(5);
     Iterator<Integer> iterator = deque.iterator();  
     while(iterator.hasNext()) {
       StdOut.println(iterator.next());
     }
     deque.removeFirst();
     iterator = deque.iterator(); 
      while(iterator.hasNext()) {
       StdOut.println(iterator.next());
     }
     deque.addFirst(10);
     iterator = deque.iterator(); 
      while(iterator.hasNext()) {
       StdOut.println(iterator.next());
     }
     deque.removeLast();
     iterator = deque.iterator(); 
      while(iterator.hasNext()) {
       StdOut.println(iterator.next());
     }

     while(iterator.hasNext()) {
       StdOut.println(iterator.next());
     }
     
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
    /*
         Deque<Integer> deque1 = new Deque<Integer>();
         deque1.addFirst(1);
         deque1.addFirst(2);

         deque1.addLast(3);
         deque1.addFirst(4);
         StdOut.println(deque1.removeFirst());
    */
    }
    //StdOut.print("ok.");
  }

