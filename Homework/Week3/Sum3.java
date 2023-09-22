import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Sum3 {
    private static int[] a;

    private static int binarySearch(int lo, int hi, int target) {
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == target) {
                return mid;
            } else if (a[mid] > target) {
                hi = mid - 1;
            } else if (a[mid] < target) {
                lo = mid + 1;
            }
        }
        return -1;
    }

    public static int countWithBinarySearch() {
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (binarySearch(j + 1, a.length - 1, -(a[i] + a[j])) != -1) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int countWithTwoPointers() {
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            int r = a.length - 1;
            for (int j = i + 1; j < a.length; j++) {
                while (r > j && a[i] + a[j] + a[r] > 0) {
                    r--;
                }
                if (a[i] + a[j] + a[r] == 0 && r > j) {
                    count++;
                }
            }
        }
        return count;
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        a = in.readAllInts();

        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] > a[j]) {
                    int tmp = a[j];
                    a[j] = a[i];
                    a[i] = tmp;
                }
            }
        }
        StdOut.println(countWithTwoPointers());
        StdOut.println(countWithBinarySearch());
    }
}
