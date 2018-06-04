import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
public class BruteCollinearPoints {
private Point[] Points;
private ArrayList<LineSegment> SegmentList = new ArrayList<LineSegment>();
    public BruteCollinearPoints(Point[] inpoints) {
        if( inpoints == null)
            throw new IllegalArgumentException("Argument is null");

        Points = new Point[inpoints.length];
        for (int i = 0;i<inpoints.length;i++){
            if (inpoints[i]==null)
                throw new IllegalArgumentException("have null points");
            Points[i] = inpoints[i];
        }
        Arrays.sort(Points);
        for (int j =0;j<inpoints.length-1;j++){
            if (Points[j].compareTo(Points[j+1])==0)
                throw new IllegalArgumentException("have same points");
         }
        findLineSegment();
    }

    private void findLineSegment() {
        for (int i = 0;i<Points.length-3;i++){
            Comparator<Point> pointI = Points[i].slopeOrder();
            for (int j = i+1; j<Points.length-2;j++){
                for (int m = j+1;m<Points.length-1;m++){
                    if (pointI.compare(Points[j],Points[m])!=0)
                        continue;
                    for (int n = m+1; n<Points.length;n++){
                        if (pointI.compare(Points[m],Points[n])==0){
                            //StdOut.print("the line: "+i+"; "+j+"; "+m+"; "+n+".");
                            SegmentList.add(new LineSegment(Points[i],Points[n]));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return SegmentList.size();
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] segments = new LineSegment[SegmentList.size()];
        int i = 0;
        for(LineSegment seg : SegmentList){
            segments[i++] = seg;
        }
        return segments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        //In in = new In("collinear/input8.txt"); //本地测试使用
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
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.print("the size is "+ collinear.numberOfSegments());
        StdDraw.show();
    }
}
