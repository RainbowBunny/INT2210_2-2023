import java.util.Comparator;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> resultSegments;
    private Point[] pointsList;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        // Pre-process points
        // Checking if there is any invalid point
        pointsList = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            pointsList[i] = points[i];
        }

        // Sorting the points
        for (int i = 0; i < pointsList.length; i++) {
            for (int j = i + 1; j < pointsList.length; j++) {
                if (pointsList[i].equals(pointsList[j])) {
                    throw new IllegalArgumentException();
                }
                if (pointsList[i].compareTo(pointsList[j]) > 0) {
                    Point tmp = pointsList[j];
                    pointsList[j] = pointsList[i];
                    pointsList[i] = tmp;
                }
            }
        }

        resultSegments = new ArrayList<>();
        solve();
    }

    private void solve() {
        for (int p1 = 0; p1 < pointsList.length; p1++) {
            Comparator<Point> comparator = pointsList[p1].slopeOrder();
            for (int p2 = p1 + 1; p2 < pointsList.length; p2++) {
                for (int p3 = p2 + 1; p3 < pointsList.length; p3++) {
                    if (comparator.compare(pointsList[p2], pointsList[p3]) == 0) {
                        for (int p4 = p3 + 1; p4 < pointsList.length; p4++) {
                            if (comparator.compare(pointsList[p3], pointsList[p4]) == 0) {
                                resultSegments.add(new LineSegment(pointsList[p1], pointsList[p4]));
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return resultSegments.size();
    }


    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[numberOfSegments()];
        for (int i = 0; i < resultSegments.size(); i++) {
            result[i] = resultSegments.get(i);
        }
        return result;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.println();
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw(); 
        }
        StdDraw.show();
    }
}
