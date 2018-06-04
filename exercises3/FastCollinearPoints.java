
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;



public class FastCollinearPoints {

    private Point [] inPoints;
    private ArrayList<Pointspair> pointspair= new ArrayList<Pointspair>();
    private ArrayList<LineSegment> SegmentList = new ArrayList<LineSegment> ();
    public FastCollinearPoints (Point [] points){
        if (points == null) throw new IllegalArgumentException("Argument is null");
        inPoints = new Point[points.length];
        for (int j= 0;j<inPoints.length;j++){
            if (points[j]==null)
                throw new IllegalArgumentException("have null points");
            inPoints[j] = points[j];
        }
        Arrays.sort(inPoints);
        for (int i =0;i<inPoints.length-1;i++){
            if (inPoints[i].compareTo(inPoints[i+1])==0)
                throw new IllegalArgumentException("have same points");
        }
        //StdOut.print(NumofCollinear);
        findLineSegment();
    }

    public           int numberOfSegments()        // the number of line segments
    {
        return pointspair.size();
    }


    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] segs = new LineSegment[pointspair.size()];
        int i = 0;
        for (Pointspair p :pointspair){
            Point a = p.getLittlePoint();
            Point b = p.getBigPoint();
            segs[i++] =new LineSegment(a,b);
        }
        return segs;
    }

    private Point[] splitPoints(Point center){
        Point[] splitPoints = new Point[inPoints.length-1];
        int i=0;
        for (Point p:inPoints){
            if (center.compareTo(p)!=0){
                splitPoints[i++]=p;
            }
        }
        return splitPoints;
    }

    private Point[] copyOfRange(Point[] a, int start, int len){
        Point[] result = new Point[len];
        for (int i = start; i<start+len ;i++){
            result[i-start] = a[i];
        }
        return result;
    }



    private void addLineSegment(ArrayList<Point> a){
        StdOut.print(pointspair.size());
        Point[] b = new Point [a.size()];
        b = a.toArray(b);
        Arrays.sort(b);
        Point N_LittlePoint = b[0];
        Point N_BigPoint = b[b.length-1];
        Point P_LittlePoint = null;
        Point P_BigPoint = null;
        Pointspair  N_pair = new Pointspair(N_LittlePoint, N_BigPoint);
        if (pointspair.size()==0)
            pointspair.add(N_pair);
        else {
            boolean isCollinear = false;
            int i = 0;
            for (Pointspair p:pointspair){
                if (N_pair.isCollinear(p)){
                    isCollinear = true;
                    P_LittlePoint = p.getLittlePoint();
                    P_BigPoint = p.getBigPoint();
                    if (N_LittlePoint.compareTo(P_LittlePoint)<0)
                        P_LittlePoint = N_LittlePoint;
                    if (N_BigPoint.compareTo(P_BigPoint)>0)
                        P_BigPoint = N_BigPoint;
                    break;
                }
                i++;
            }
            if (isCollinear == false)
                pointspair.add(new Pointspair(N_LittlePoint,N_BigPoint));
            else{
                pointspair.remove(i);
                pointspair.add(new Pointspair(P_LittlePoint,P_BigPoint));

            }
        }
    }

    private void findLineSegment(){
        for (int h=0;h<inPoints.length-3;h++){
            Point center = inPoints[h];
            Point [] splitPoints= new Point[inPoints.length-h-1];
            splitPoints= copyOfRange(inPoints,h+1,inPoints.length-h-1);
            Comparator<Point> com = new SlopeOrder(center);
            //StdOut.print(h);
            Arrays.sort(splitPoints,com);
            ArrayList<Point> count = new ArrayList<Point> ();
            Point cursor = splitPoints[0];
            for (int i = 0; i< splitPoints.length;i++){
                if (i==0){
                    cursor = splitPoints[i];
                    count.add(splitPoints[i]);
                }
                else {
                    if (com.compare(cursor,splitPoints[i])==0) {
                        count.add(splitPoints[i]);
                    }
                    else {
                        if(count.size()>=3){
                            count.add(center);
                            addLineSegment(count);
                        }
                        cursor = splitPoints[i];
                        count.clear();
                        count.add(splitPoints[i]);
                    }
                }
                if (i==splitPoints.length-1){
                    if(count.size()>=3){
                        count.add(center);
                        addLineSegment(count);
                    }
                    count.clear();
                }

            }
        }
    }

    private class SlopeOrder implements Comparator<Point>{
        private Point center;
        private SlopeOrder (Point inputcenter){
            center=inputcenter;
        }
        public int compare (Point Point1, Point Point2){
            double slope1=getSlope(center,Point1);
            double slope2=getSlope(center,Point2);
            if (slope1==slope2) return 0;
            if (slope1<slope2) return -1;
            else return 1;
        }
        private double getSlope(Point a, Point b){
            double slope;
            if(a.compareTo(b)<0) slope =a.slopeTo(b);
            else slope = b.slopeTo(a);
            return slope;
        }

    }

    private class Pointspair {
        private Point LittlePoint;
        private Point BigPoint;
        public Pointspair(Point a, Point b){
            if (a.compareTo(b)<=0){
                LittlePoint = a;
                BigPoint = b;
            }
            else {
                LittlePoint=b;
                BigPoint =a;
            }
        }
        public Point getLittlePoint(){
            return LittlePoint;
        }
        public Point getBigPoint(){
            return BigPoint;
        }
        public void setLittlePoint(Point newLittlePoint){
            this.LittlePoint = newLittlePoint;
        }

        public void setBigPoint(Point newBigPoint){
            this.BigPoint = newBigPoint;
        }
        public boolean isCollinear (Pointspair that){
            Point T_LittlePoint = that.getLittlePoint();
            Point T_BigPoint = that.getBigPoint();
            if (T_LittlePoint.compareTo(this.LittlePoint)==0 && T_BigPoint.compareTo(this.BigPoint)==0)
                return true;
            else {
                boolean result = true;
                Point [] store = {T_LittlePoint,T_BigPoint,this.LittlePoint,this.BigPoint};
                Arrays.sort(store);
                ArrayList <Point> distinct= new ArrayList<Point>();
                distinct.add(store[0]);
                for (int i=1;i<store.length;i++){
                    if (store[i].compareTo(distinct.get(distinct.size()-1))!=0)
                            distinct.add(store[i]);
                }
                Point [] dis_points = new Point[distinct.size()];
                dis_points= distinct.toArray(dis_points);

                Comparator<Point> com = dis_points[0].slopeOrder();
                for (int i=2;i<dis_points.length;i++){
                    if (com.compare(dis_points[i],dis_points[i-1])!=0){
                        result = false;
                    }
                }
                return result;
            }
        }
    }

    public static void main(String[] args) {
        /*
           In in = new In("./data/input8.txt");
           int n = in.readInt();
           Point[] points = new Point[n];
           for (int i = 0; i < n; i++) {
           int x = in.readInt();
           int y = in.readInt();
           points[i] = new Point(x, y);
           }
           FastCollinearPoints collinear =new FastCollinearPoints(points);
           */

        // read the n points from a file
        In in = new In(args[0]);
        //In in = new In("./data/input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        // LineSegment [] segments= (LineSegment []) collinear.segments();
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.print(collinear.numberOfSegments());
        StdDraw.show();

    }
}


