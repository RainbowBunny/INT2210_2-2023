import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

public class UFClient2 { 
    public static void main(String[] args) { 
        int N = StdIn.readInt(); 
        UF uf = new UF(N); 
        int currentSize = N, countEdges = 0;
        while (!StdIn.isEmpty()) {
            countEdges++;
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            uf.union(p, q);
            if (uf.count() == 1) {
                StdOut.println(countEdges);
                return;
            }
        }
        StdOut.println("Failed");
    }
}