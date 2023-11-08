import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

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

    public static int search(ArrayList<Long> currentList, long value) {
        int l = 0, r = currentList.size() - 1;
        if (currentList.get(currentList.size() - 1) < value) {
            return currentList.size();
        }
        while (l < r) {
            int mid = (l + r) >> 1;
            if (currentList.get(mid) < value) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public static long count2(long[] arr) {
        ArrayList<Long> current1 = new ArrayList<>();
        ArrayList<Long> current2 = new ArrayList<>();

        current1.add((long) 0);
        current2.add((long) 0);
        for (int i = 0; i < arr.length / 2; i++) {
            int currentSize = current1.size();
            for (int j = 0; j < currentSize; j++) {
                current1.add(current1.get(j) + arr[i]);
            }
        }

        current1.sort(new Comparator<Long>() {
            @Override
            public int compare(Long lhs, Long rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.compareTo(rhs);
            }
        });

        long ans = 0;
        for (int i = arr.length / 2; i < arr.length; i++) {
            int currentSize = current2.size();
            for (int j = 0; j < currentSize; j++) {
                current2.add(current2.get(j) + arr[i]);
            }
        }

        for (int i = 0; i < current2.size(); i++) {
            ans += search(current1, current2.get(i) + 1) - search(current1, current2.get(i));
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
        StdOut.println(count2(arr));
    }
}