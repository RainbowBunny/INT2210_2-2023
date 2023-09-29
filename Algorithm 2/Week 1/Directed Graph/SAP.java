import java.util.ArrayList;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);
    }

    private int caculate(Iterable<Integer> v, Iterable<Integer> w, int task) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        } 

        int[] distance1 = new int[digraph.V()], distance2 = new int[digraph.V()];
        for (int i = 0; i < digraph.V(); i++) {
            distance1[i] = -1;
            distance2[i] = -1;
        }
        Queue<Integer> bfsQueue1 = new Queue<>(), bfsQueue2 = new Queue<>();
        for (Integer x : v) {
            if (x == null || x < 0 || x >= digraph.V()) {
                throw new IllegalArgumentException();
            }
            distance1[x] = 0;
            bfsQueue1.enqueue(x);
        }
        for (Integer x : w) {
            if (x == null || x < 0 || x >= digraph.V()) {
                throw new IllegalArgumentException();
            }
            distance2[x] = 0;
            bfsQueue2.enqueue(x);
        }
        while (!bfsQueue1.isEmpty()) {
            int node = bfsQueue1.dequeue();
            for (int adj : digraph.adj(node)) {
                if (distance1[adj] != -1) {
                    continue;
                }
                distance1[adj] = distance1[node] + 1;
                bfsQueue1.enqueue(adj);
            }
        }
        while (!bfsQueue2.isEmpty()) {
            int node = bfsQueue2.dequeue();
            for (int adj : digraph.adj(node)) {
                if (distance2[adj] != -1) {
                    continue;
                }
                distance2[adj] = distance2[node] + 1;
                bfsQueue2.enqueue(adj);
            }
        }
        int resultNode = -1, resultDistance = 2 * distance1.length;
        for (int i = 0; i < distance1.length; i++) {
            if (distance1[i] == -1 || distance2[i] == -1) {
                continue;
            }
            
            if (resultNode == -1 || (resultDistance > distance1[i] + distance2[i])) {
                resultNode = i;
                resultDistance = distance1[resultNode] + distance2[resultNode];
            }
        }
        if (task == 1) {
            return resultNode;
        } else {
            if (resultNode == -1) {
                return -1;
            }
            return resultDistance;
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> x = new ArrayList<>(), y = new ArrayList<>();
        x.add(v);
        y.add(w);
        return caculate(x, y, 2);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> x = new ArrayList<>(), y = new ArrayList<>();
        x.add(v);
        y.add(w);
        return caculate(x, y, 1);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return caculate(v, w, 2);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return caculate(v, w, 1);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}