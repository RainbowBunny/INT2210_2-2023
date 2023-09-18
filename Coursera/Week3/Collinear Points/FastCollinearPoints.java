import java.util.ArrayList;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private ArrayList<LineSegment> resultSegments;
    private Point[] pointsList;
    private boolean[][] checked;

    public FastCollinearPoints(Point[] points) {
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
        checked = new boolean[pointsList.length][pointsList.length];

        solve();
    }

    private void merge(int[] sortedIndex, int[] auxIndex, int lo, int hi, int mid, Comparator<Point> comparator) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                sortedIndex[k] = auxIndex[j++];
            } else if (j > hi) {
                sortedIndex[k] = auxIndex[i++];
            } else {
                if (comparator.compare(pointsList[auxIndex[i]], pointsList[auxIndex[j]]) < 0 ||
                 (comparator.compare(pointsList[auxIndex[i]], pointsList[auxIndex[j]]) == 0 && auxIndex[i] < auxIndex[j])) {
                    sortedIndex[k] = auxIndex[i++];
                } else {
                    sortedIndex[k] = auxIndex[j++];
                }
            }
        }
    }

    private void mergeSort(int[] sortedIndex, int[] auxIndex, int lo, int hi, Comparator<Point> comparator) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;

        mergeSort(auxIndex, sortedIndex, lo, mid, comparator);
        mergeSort(auxIndex, sortedIndex, mid + 1, hi, comparator);
        merge(sortedIndex, auxIndex, lo, hi, mid, comparator);
    }

    private void solve() {
        int[] currentIndex = new int[pointsList.length - 1];
        int[] auxIndex = new int[pointsList.length - 1];
        for (int i = 0; i < pointsList.length; i++) {
            int currentCount = 0;
            Comparator<Point> comparator = pointsList[i].slopeOrder();
            for (int j = 0; j < pointsList.length; j++) {
                if (i == j || checked[i][j]) {
                    continue;
                }

                currentIndex[currentCount] = j;
                auxIndex[currentCount] = j;
                currentCount++;
            }

            mergeSort(currentIndex, auxIndex, 0, currentCount - 1, comparator);

            int currentPosition = 0;
            while (currentPosition < currentCount) {
                int smaller = 0;
                for (int j = currentPosition; j <= currentCount; j++) {
                    if (j == currentCount || 
                        comparator.compare(pointsList[currentIndex[currentPosition]], pointsList[currentIndex[j]]) != 0) {
                        if (smaller == 0 && j - currentPosition >= 3) {
                            for (int p1 = currentPosition; p1 < j; p1++) {
                                checked[i][currentIndex[p1]] = true;
                                checked[currentIndex[p1]][i] = true;
                                for (int p2 = currentPosition; p2 < j; p2++) {
                                    checked[currentIndex[p1]][currentIndex[p2]] = true;
                                    checked[currentIndex[p2]][currentIndex[p1]] = true;
                                }
                            }
                            resultSegments.add(new LineSegment(pointsList[i], pointsList[currentIndex[j - 1]]));
                        }
                        currentPosition = j;
                        break;
                    } else {
                        if (i > currentIndex[j]) {
                            smaller = 1;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
