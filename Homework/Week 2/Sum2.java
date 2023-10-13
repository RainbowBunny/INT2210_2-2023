import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Sum2 {
    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] a = in.readAllInts();

        int count = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] + a[j] == 0) {
                    count++;
                }
            }
        }
        StdOut.println(count);
    }
}
