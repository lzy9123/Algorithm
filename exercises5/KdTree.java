import java.util.Comparator;
import java.util.TreeSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import java.lang.IllegalArgumentException;
public class KdTree {
    // construct an empty set of points
    private Node root;
    private int size;
    private Node nearestP;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        boolean isVertical;
    }



    public         KdTree() {
        root = null;
        size = 0;
    }




    // is the set empty?
    public           boolean isEmpty(){
        return root == null;
    }

    // number of points in the set
    public               int size(){
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p){
        if(p == null) throw new IllegalArgumentException("null arguments.");
        if (root == null){
            root = new Node();
            root.p = p;
            root.rect = new RectHV (0,0,1,1);
            root.lb = null;
            root.rt = null;
            root.isVertical = true;
            size++;
        }
        else{
            if(!contains(p)){
                put(root, p);
                size++;
        }
    }
    }

    private void put(Node cursor, Point2D p){
            Comparator<Point2D> cmp = cmp(cursor);
            if (cmp.compare(p, cursor.p)<0){
                if(cursor.lb == null){
                    Node newNode = new Node();
                    newNode.p = p;
                    newNode.isVertical = flipisVertical(cursor);

                    double xmin,ymin,xmax,ymax;
                    //Construct a Rect;
                    xmin = cursor.rect.xmin();
                    ymin = cursor.rect.ymin();
                    if (cursor.isVertical){
                        xmax = cursor.p.x();
                        ymax = cursor.rect.ymax();
                    }
                    else{
                        xmax = cursor.rect.xmax();
                        ymax = cursor.p.y();
                    }
                    newNode.rect = new RectHV(xmin,ymin,xmax, ymax);
                    //rect.draw();

                    newNode.lb = null;
                    newNode.rt = null;

                    cursor.lb = newNode;
                }
                else{
                    put (cursor.lb, p);
                }
            }
            else{
                if(cursor.rt == null){
                    Node newNode = new Node();
                    newNode.p = p;
                    newNode.isVertical = flipisVertical(cursor);

                    //Construct a Rect;
                    double xmax,ymax,xmin,ymin;
                    xmax = cursor.rect.xmax();
                    ymax = cursor.rect.ymax();
                    if (cursor.isVertical){
                        xmin = cursor.p.x();
                        ymin = cursor.rect.ymin();
                    }
                    else{
                        xmin = cursor.rect.xmin();
                        ymin = cursor.p.y();
                    }
                    newNode.rect = new RectHV(xmin, ymin, xmax, ymax);
                    //rect.draw();

                    newNode.lb = null;
                    newNode.rt = null;

                    cursor.rt = newNode;


                }
                else{
                    put (cursor.rt, p);
                }
            }

    }

    private boolean flipisVertical(Node cursor){
        if(cursor.isVertical)
            return false;
        else
            return true;
    }


    private Comparator<Point2D> cmp (Node p){
            if(p.isVertical)
                return Point2D.X_ORDER;
            else
                return Point2D.Y_ORDER;
    }
    // does the set contain point p?
    public           boolean contains(Point2D p){
        if(p == null) throw new IllegalArgumentException("Contains: null point");
        return isContains(root, p);
    }

    private boolean isContains(Node cursor, Point2D p){
        if(cursor == null){
            return false;
        }
        else{
            Comparator<Point2D> cmp = cmp(cursor);
            if(cursor.p.equals(p))
                    return true;
            else if(cmp.compare(p,cursor.p) < 0){
                return isContains(cursor.lb, p);
            }
            else{
                return isContains(cursor.rt, p);
            }
        }
    }

    // draw all points to standard draw
    public              void draw(){
        drawAll(root);
    }

    private void drawAll(Node cursor){
        if(cursor == null){
            return;
        }
        else{
            drawAll(cursor.lb);
            drawAll(cursor.rt);

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            cursor.p.draw();
            if(cursor.isVertical){
                StdDraw.setPenRadius(0.005);
                StdDraw.setPenColor(StdDraw.BLUE);
                cursor.rect.draw();
            }
            else{

                StdDraw.setPenRadius(0.005);
                StdDraw.setPenColor(StdDraw.RED);
                cursor.rect.draw();
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) throw new IllegalArgumentException("range: null argument");
        Queue<Point2D> queue = new Queue<Point2D>();
        getRange(queue,root,rect);
        return queue;

    }


    private void getRange(Queue<Point2D> queue, Node cursor, RectHV rect){
        if(cursor == null)
            return;
        else{
            if(cursor.rect.intersects(rect)){
                getRange(queue, cursor.lb,rect);
                getRange(queue, cursor.rt,rect);
                if(rect.contains(cursor.p))
                    queue.enqueue(cursor.p);
            }
            else
                return;
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public           Point2D nearest(Point2D p){
        if (p == null) throw new IllegalArgumentException("nearest: null argument");
        if (isEmpty())
            return null;
        nearestP = root;
        getNearest(root, p);
        return nearestP.p;
    }

    private void getNearest(Node cursor, Point2D p){
        if (cursor == null){
            return ;
        }
        else{
            if(GetDistanceToRect(cursor, p) > nearestP.p.distanceTo(p)){
                return ;
            }
            else{
                if(cursor.p.distanceTo(p) < nearestP.p.distanceTo(p))
                        nearestP = cursor;
                Comparator<Point2D> cmp = cmp(cursor);
                if(cmp.compare(p,cursor.p) < 0){
                    getNearest(cursor.lb, p);
                    getNearest(cursor.rt, p);

                }
                else{
                    getNearest(cursor.rt, p);
                    getNearest(cursor.lb, p);
                }
            }
        }
    }




    private double GetDistanceToRect(Node cursor, Point2D p){
        if (cursor.rect.contains(p))
            return -1.0;
        else
            return cursor.rect.distanceTo(p);
    }



public static void main(String[] args){
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        StdDraw.clear();
        kdtree.draw();

}
}
