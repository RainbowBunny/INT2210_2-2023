import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

public class UFClient1 { 
    public static void main(String[] args) { 
        int N = StdIn.readInt(); 
        UF uf = new UF(N); 
        int currentSize = N;
        while (!StdIn.isEmpty()) { 
            int p = StdIn.readInt(); 
            int q = StdIn.readInt();
            uf.union(p, q);
            if (uf.count() < currentSize) {
                currentSize--;
                StdOut.println(p + " " + q);
            }
        } 
    }
}