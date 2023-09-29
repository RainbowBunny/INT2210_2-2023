import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;
import java.util.ArrayList;

public class Solver {
    private boolean solveable;
    private Board initial;
    private Stack<Board> solution;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
        solution = new Stack<>();
        solve();
    }

    private class Node {
        private Board board;
        private Node parent;
        private int priority;
        private int step;

        public Node(Board board, Node parent, int priority, int step) {
            this.board = board;
            this.parent = parent;
            this.priority = priority;
            this.step = step;
        }

        public Board getBoard() {
            return board;
        }

        public Node getParent() {
            return parent;
        }

        public int getPriority() {
            return priority;
        }

        public int getStep() {
            return step;
        }
    }

    private class SortByPriority implements Comparator<Node> {
        public int compare(Node o1, Node o2) {
            if (o1.getPriority() != o2.getPriority()) {
                return o1.getPriority() < o2.getPriority() ? -1 : 1;
            }
            return 0;
        } 
    }

    private void solve() {
        ArrayList<MinPQ<Node>> pq = new ArrayList<MinPQ<Node>>();
        for (int i = 0; i < 2; i++) {
            pq.add(new MinPQ<>(new SortByPriority()));
        }

        pq.get(0).insert(new Node(initial, null, initial.manhattan(), 0));
        Board twin = initial.twin();
        pq.get(1).insert(new Node(twin, null, twin.manhattan(), 0));

        while (pq.get(0).size() > 0 && pq.get(1).size() > 0) {
            for (int i = 0; i < 2; i++) {
                Node current = pq.get(i).delMin();
                if (current.getBoard().isGoal()) {
                    if (i == 1) {
                        solveable = false;
                    } else {
                        solveable = true;
                        solution = new Stack<Board>();
                        while (current != null) {
                            solution.push(current.getBoard());
                            current = current.getParent();
                        }
                    }
                    return;
                }

                for (Board neighbor : current.getBoard().neighbors()) {
                    if (current.getParent() != null && current.getParent().getBoard().equals(neighbor)) {
                        continue;
                    }

                    pq.get(i).insert(new Node(neighbor, current, neighbor.manhattan() + current.getStep() + 1, current.getStep() + 1));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solveable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solveable) return -1;
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solveable) return null;
        return solution;
    }
    

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}