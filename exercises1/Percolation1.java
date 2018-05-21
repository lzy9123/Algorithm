/**
 * Auto Generated Java Class.
 */
//import UF;
import java.util.*;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  int[][]a;
  UF uf;
  //int link[][];
  //int num;
  public Percolation(int n)
  {
  if(n<=0) throw new java.lang.IllegalArgumentException("n cannot less than 1.");
  a=new int[n][n];
  for(int i=0;i<n;i++)
  {
    for(int j=0;j<n;j++)
    {
     a[i][j]=0;
  }
    
  }
  uf= new UF( a.length*a[0].length);
  
  
  //a={1,0,0,1,0,0,1,0,0};
  }
  
  
  public    void open(int row, int col)    
  {
  if (row>a.length||col>a[0].length) throw new java.lang.IllegalArgumentException("Open: Out of Exception");
  int i=row-1;
  int j=col-1;
  if (a[i][j]!=1) 
  {
  a[i][j]=1;
  if(row>1) {if(isOpen(row-1,col)) uf.connect((i-1)*a.length+j,i*a.length+j);}
  if(col>1) {if(isOpen(row,col-1)) uf.connect(i*a.length+j-1,i*a.length+j);}
  if(row<a.length) {if(isOpen(row+1,col)) uf.connect(i*a.length+j,(i+1)*a.length+j);}
  if(col<a.length){if(isOpen(row,col+1)) uf.connect(i*a.length+j,i*a.length+j+1);}
  }
  }
  public boolean isOpen(int row, int col)  
  {
  if (row>a.length||col>a[0].length) throw new java.lang.IllegalArgumentException("isOpen: Out of Exception");
  return a[row-1][col-1]==1;
  }
  public     int numberOfOpenSites()
  {
  int num=0;
  System.out.println(a.length);
    for(int i=0;i<a.length;i++)
  {
    for(int j=0;j<a[0].length;j++)
    {
      if (a[i][j]==0);num+=1;
  }
  }
  return num; 
  }
public void initial()
 {
  a[0][0]=1;
  a[0][1]=1;
  a[0][2]=1;
  a[1][2]=1;
  a[2][2]=1;
 }

public void link()
{
  for (int i=0;i<a.length;i++)
  {
    for(int j=0;j<a[0].length;j++)
    {
     if(a[i][j]==1&&i<a.length-1)
      {
       if(a[i+1][j]==1)
        {
             uf.connect(i*a.length+j,(i+1)*a.length+j);
        }
     }
     if(a[i][j]==1&&j<a[0].length-1)
     {
        if(a[i][j+1]==1)
        {
             uf.connect(i*a.length+j,i*a.length+j+1);
        }
        }
    }
      
   }

}


  public boolean isFull(int row, int col)
  {
    if (row>a.length||col>a[0].length) throw new java.lang.IllegalArgumentException("isFull: Out of Exception");
    //System.out.println(row+" "+col+" "+uf.root((row-1)*a.length+col-1));
    int q=uf.root((row-1)*a.length+col-1);
    return q<=a.length-1 && isOpen(q/a.length+1,q%a.length+1);
    
  } 
  
  public boolean percolates()
  {
  boolean p=false;
  int i=1;
  while(p!=true&&i<a.length)
  {
  //StdOut.println("aa"+i);
  p=isFull(a.length,i);
  i++;
  }
  return p;
  }
  public static void main(String[] args)   
  {
  Percolation a;
  a=new Percolation(10);
  //a.initial();
  //a.link();
  //System.out.println(a.numberOfOpenSites());
  //System.out.println(a.isFull(3,3));
  System.out.println(a.isFull(1,1));
  System.out.println(a.isFull(1,2));
  System.out.println(a.isFull(1,3));
  a.open(1,1);
  a.open(1,2);
  System.out.println(a.isFull(1,2));
  a.open(1,3);
  System.out.println(a.isFull(1,3));
  a.open(2,3);
  System.out.println(a.isFull(2,3));
  a.open(3,2);
  System.out.println(a.isFull(3,2));
  //System.out.println(a.isOpen(1,1));
  //System.out.println(a.isFull(3,2));
  //System.out.println(a.isFull(2,3));
  //System.out.println(a.isFull(3,2));
  
  
  a.open(3,3);
  System.out.println(a.isFull(2,3));
  System.out.println(a.isFull(3,3));                   
  System.out.println(a.percolates());
  //a.open(3,11);
  //System.out.println(a.isFull(3,11));
  //System.out.println(a.isOpen(3,11));
  }
  /* ADD YOUR CODE HERE */
  
}
