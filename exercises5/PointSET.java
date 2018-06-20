import java.util.TreeSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import java.lang.IllegalArgumentException;

public class PointSET {
    private TreeSet<Point2D> PointSET;
    // construct an empty set of points
    public         PointSET() {
        PointSET = new TreeSet<Point2D>();
    }

    // is the set empty?
    public           boolean isEmpty(){
        return PointSET.isEmpty();
    }

    // number of points in the set
    public               int size(){
        return PointSET.size();
    }

    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p){
        if(p == null) throw new IllegalArgumentException("null arguments.");
        if(!contains(p))
            PointSET.add(p);
    }

    // does the set contain point p?
    public           boolean contains(Point2D p){
            return PointSET.contains(p);
    }

    // draw all points to standard draw
    public              void draw(){
        for(Point2D p:PointSET){
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        Queue<Point2D> queue = new Queue<Point2D> ();
        for (Point2D p:PointSET){
            if(isInsideRect(p, rect))
                queue.enqueue(p);
        }
        return queue;
    }

    private boolean isInsideRect(Point2D p, RectHV rect){
        double px = p.x();
        double py = p.y();
        return px >= rect.xmin() && px <= rect.xmax() && py >= rect.ymin() && py <= rect.ymax();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public           Point2D nearest(Point2D p){
        double distance = -1.0;
        Point2D nearestP = null;
        for (Point2D ps:PointSET){
            if(distance < 0){
                distance = ps.distanceTo(p);
                nearestP = ps;
            }
            else{
                if (distance > ps.distanceTo(p)){
                distance = getDistance(ps,p);
                nearestP = ps;
                }
            }
        }
        return nearestP;
    }

    private double getDistance(Point2D p1, Point2D p2){
        return Math.sqrt(Math.pow(p1.x()-p2.x(),2) +  Math.pow(p1.y()-p2.y(),2));
    }

}
