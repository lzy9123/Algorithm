import java.util.*;
import edu.princeton.cs.algs4.StdOut;
public class UF
{
private int[] id;
private int[] sz;
public UF (int N)
{
id=new int[N];
sz=new int[N];
for (int i = 0;i<N;i++) {id[i]=i;sz[i]=1;}
//System.out.println(id[0]);
}

public void connect(int p, int q)
{
  int p1=root(p);
  int q1=root(q);           
  if (p1<=q1) {id[q1]=p1;}
  else {id[p1]=q1;}
  StdOut.println("Connect "+p+" "+q);
  StdOut.println("Root of "+q+"="+root(q));
}

public int root(int i)
{
if (i!=id[i]) return root(id[i]);
else return i;
}

public boolean connect_or_not(int p, int q)
  
{
if (root(p)==root(q)) return true;
else return false;
}
  
public static void main(String[] args)
{
UF uf = new UF(10);
System.out.println(uf.root(3)+" "+ uf.root(5));
//uf.connect (1,2);
uf.connect (3,4);
uf.connect (4,5);
System.out.println(uf.root(3)+" "+ uf.root(5));
uf.connect (0,5);
System.out.println(uf.root(3)+" "+uf.root(5));
}

}


