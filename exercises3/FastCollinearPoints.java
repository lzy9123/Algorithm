import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;



public class FastCollinearPoints {

    private int NumofSegments=0;
    private int NumofCollinear=0;
    private Point [] inPoints;
    private Comparator<Point> comparator;
    private Point[][] collinearL= new Point[1][2];
    private collinear c;
    private Point[] getOrderPoints;
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
            if (inPoints[i]==null)
                throw new IllegalArgumentException("have null points");
            if (inPoints[i].compareTo(inPoints[i+1])==0)
                throw new IllegalArgumentException("have same points");
            if (i==inPoints.length-2 && inPoints[i]==null)
                throw new IllegalArgumentException("have null points");
        }
        int N=points.length;
        double slope;
        Point[] end;
        for (int h=0;h<N;h++){
            getOrderPoints= getOrderPoints(inPoints[h]);
            c = new collinear (inPoints[h],getOrderPoints);
            c.getCollinearPoints();
        }

        //StdOut.print(NumofCollinear);

    }

    public           int numberOfSegments()        // the number of line segments
    {
        return NumofSegments;
    }

    private void addCollinearL(Point[] _end){
        if (NumofCollinear>=collinearL.length){
            resize(2*collinearL.length);
        }
        NumofCollinear++;
        collinearL[NumofCollinear-1][0]=_end[0];
        collinearL[NumofCollinear-1][1]=_end[1];
    }

    private void resize (int size){
        Point[][] temp = new Point [size][2];
        for (int i=0;i<size/2;i++){
            temp[i][0]=collinearL[i][0];
            temp[i][1]=collinearL[i][1];
        }
        collinearL=temp;
    }


    public LineSegment[] segments()                // the line segments
    {
        Point [][] distinctLine =new Point [NumofCollinear][2];
        for (int i=0;i<NumofCollinear;i++){
            if (i==0){
                distinctLine[0][0]=collinearL[0][0];
                distinctLine[0][1]=collinearL[0][1];
                NumofSegments++;
            }
            else {
                int j=NumofSegments-1;
                for (;j>=0;j--){
                    if (isCollinear(distinctLine[j],collinearL[i])){
                        if (collinearL[i][0].compareTo(distinctLine[j][0])==-1)
                            distinctLine[j][0]=collinearL[i][0];
                        if (collinearL[i][1].compareTo(distinctLine[j][1])==1)
                            distinctLine[j][1]=collinearL[i][1];
                        break;
                    }
                }
                if (j==-1){
                    distinctLine[NumofSegments][0]=collinearL[i][0];
                    distinctLine[NumofSegments][1]=collinearL[i][1];
                    NumofSegments++;
                }
            }
        }
        LineSegment[] segments= new LineSegment[NumofSegments];
        for (int k=0;k<NumofSegments;k++){
            segments[k]= new LineSegment(distinctLine[k][0],distinctLine[k][1]);
        }
        return segments;
    }




    public boolean isCollinear (Point []_ends1, Point[] _ends2){
        if (_ends1[0].compareTo(_ends2[0])==0 && _ends1[1].compareTo(_ends2[1])==0){
            return true;
        }
        else {
            boolean result = true;
            Point[] points= new Point [4];
            points [0]= _ends1[0];
            points [1]= _ends1[1];
            points [2]= _ends2[0];
            points [3]= _ends2[1];
            Arrays.sort(points);
            points=dropDuplicate(points);
            Comparator<Point> com = slopeOrder(points[0]);
            for (int i=2;i<points.length;i++){
                if (com.compare(points[i],points[i-1])!=0){
                    result = false;
                }
            }
            return result;
        }
    }

    private Point[] dropDuplicate(Point [] _points){
        ArrayList <Point> distinct= new ArrayList<Point>();
        int numofdistinct=0;
        for (int i =0 ;i<_points.length;i++){
            if (i==0) {
                distinct.add(_points[i]);
            }
            else {
                if(_points[i].compareTo(distinct.get(distinct.size()-1))!=0)
                    distinct.add(_points[i]);
            }
        }
        Point[] a= new Point[distinct.size()];
        a=distinct.toArray(a);
        return a;
    }



    private class collinear {
        private Point center;
        private Point [] points;
        private ArrayList<Point> collinearP= new ArrayList<Point>();
        //private Point[][] collinearL;
        private Point [] end;
        Point cursor;
        private Comparator <Point> comparator;
        public collinear(Point p, Point[] _points){
            center = p;
            points = _points;
            getSlopeOrder();
        }

        private void  getSlopeOrder(){
            comparator = FastCollinearPoints.this.slopeOrder(center);
            Arrays.sort(points, comparator);

        }

        private void getCollinearPoints(){
            comparator= FastCollinearPoints.this.slopeOrder(center);
            for (int i=0;i<points.length;i++){
                if (center.compareTo(points[i])==0) throw new IllegalArgumentException("Include the same point");
                //Numofsameslope=0;
                if (i==0)  {
                    cursor = points[i];
                    collinearP.add(points[i]);
                }
                else {
                    if (comparator.compare(cursor,points[i])==0) {
                        collinearP.add(points[i]);
                    }
                    else {
                        if(collinearP.size()>=3){
                            end = endsofCollinearL(collinearP);
                            FastCollinearPoints.this.addCollinearL(end);
                        }
                        cursor = points[i];
                        collinearP.clear();
                        collinearP.add(points[i]);
                    }
                }
                if (i==points.length-1){
                    if(collinearP.size()>=3){
                        end = endsofCollinearL(collinearP);
                        FastCollinearPoints.this.addCollinearL(end);
                    }
                    collinearP.clear();
                }
            }
        }

            private Point[] endsofCollinearL(ArrayList<Point> _collinearP){
                ArrayList<Point> p=_collinearP;
                p.add(center);
                Point[] a = new Point [p.size()];
                a = p.toArray(a);
                Arrays.sort(a);
                Point[] end= new Point[2];
                end[0]=a[0];
                end[1]=a[a.length-1];
                //StdOut.print(end[0].toString()+" "+end[1].toString());
                return end;
            }

        }



        private Point[] getOrderPoints(Point center){
            Point[] getOrderPoints = new Point[inPoints.length-1];
            int i=0;
            for (Point p:inPoints){
                if (p==null) throw new IllegalArgumentException("Include null Points.");
                if (center.compareTo(p)!=0){
                    getOrderPoints[i++]=p;
                    //StdOut.print(p.toString());
                }
            }
            return getOrderPoints;
        }

        private Comparator<Point> slopeOrder(Point center) {
            return new SlopeOrder(center);
            /* YOUR CODE HERE */
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
                if (a==null || b==null) throw new IllegalArgumentException("Include null Points.");
                double slope;
                if(a.compareTo(b)==0) throw new IllegalArgumentException("Include the same point");
                if(a.compareTo(b)<0) slope =a.slopeTo(b);
                else slope = b.slopeTo(a);
                return slope;
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

            /*
             * Implement the slopeOrder() method in Point.
             * The complicating issue is that the comparator needed to compare the slopes
             * that two points q and r make with a third point p, which changes from sort to sort.
             * To do this, create a private nested (non-static) class SlopeOrder that implements
             * the Comparator<Point> interface. This class has a single method compare(q1, q2)
             * that compares the slopes that q1 and q2 make with the invoking object p.
             * the slopeOrder() method should create an instance of this nested class and return it.
             * Implement the sorting solution.
             * Watch out for corner cases. Don't worry about 5 or more points on a line segment yet.
             */

        }
    }


