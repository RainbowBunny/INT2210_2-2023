import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openSites = 0, gridSize, offset;
    private boolean[][] sitesGrid;
    private WeightedQuickUnionUF unionUF;
    static private int[] dx = {1, 0, -1, 0};
    static private int[] dy = {0, 1, 0, -1};
    private boolean percolate;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        gridSize = n;
        sitesGrid = new boolean[n][n];
        unionUF = new WeightedQuickUnionUF(2 * n * n + 2);
        offset = n * n + 1;
        percolate = false;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
            throw new IllegalArgumentException();
        }
        if (isOpen(row, col)) {
            return;
        }
        openSites++;
        row--; 
        col--;
        sitesGrid[row][col] = true;
        if (row == 0) {
            unionUF.union(row * gridSize + col, gridSize * gridSize);
        }

        for (int i = 0; i < dx.length; i++) {
            int adjX = row + dx[i];
            int adjY = col + dy[i];
            if (0 <= adjX && 0 <= adjY && adjX < gridSize && adjY < gridSize) {
                if (isOpen(adjX + 1, adjY + 1)) {
                    unionUF.union(adjX * gridSize + adjY, row * gridSize + col);
                    unionUF.union(adjX * gridSize + adjY + offset, row * gridSize + col + offset);
                }
            }
        }

        if (row == gridSize - 1) {
            unionUF.union(row * gridSize + col + offset, gridSize * gridSize + offset);
        }

        if (isFull(row + 1, col + 1) && unionUF.find(row * gridSize + col + offset) == unionUF.find(gridSize * gridSize + offset)) {
            percolate = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
            throw new IllegalArgumentException();
        }
        row--; 
        col--;
        return sitesGrid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
            throw new IllegalArgumentException();
        }
        row--; 
        col--;
        return unionUF.find(row * gridSize + col) == unionUF.find(gridSize * gridSize);     
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolate;
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
