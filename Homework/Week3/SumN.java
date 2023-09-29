import java.util.HashMap;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SumN {
    private static long[] arr;

    public static long count(long[] arr) {
        HashMap<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < 1 << Math.min(20, arr.length); i++) {
            long sum = 0;
            for (int j = 0; j < Math.min(20, arr.length); j++) {
                if ((i & (1 << j)) != 0) {
                    sum += arr[j];
                }
            }
            if (map.get(sum) == null) {
                map.put(sum, 1);
            } else {
                map.put(sum, map.get(sum) + 1);
            }
        }

        long ans = 0;
        for (int i = 0; i < 1 << Math.max(0, arr.length - 20); i++) {
            long sum = 0;
            for (int j = 0; j < Math.max(0, arr.length - 20); j++) {
                if ((i & (1 << j)) != 0) {
                    sum += arr[j + 20];
                }
            }
            ans += map.get(sum);
        }
        return ans;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = StdIn.readLong();
        }       
        StdOut.println(count(arr));
    }
}