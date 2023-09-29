import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int boardSize, emptyCellRow, emptyCellColumn, hammingDistance, manhattanDistance;
    private int[][] boardTiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        boardSize = tiles.length;
        hammingDistance = 0;
        manhattanDistance = 0;

        boardTiles = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardTiles[i][j] = tiles[i][j];
                if (boardTiles[i][j] == 0) {
                    emptyCellRow = i;
                    emptyCellColumn = j;
                    continue;
                }
                int currentX = getRow(boardTiles[i][j]);
                int currentY = getColumn(boardTiles[i][j]);
                if (Math.abs(i - currentX) + Math.abs(j - currentY) > 0) {
                    hammingDistance++;
                }
                manhattanDistance += Math.abs(i - currentX) + Math.abs(j - currentY);
            }
        }
    }

    private int getRow(int position) {
        if (position < 0 || position >= boardSize * boardSize) {
            throw new IllegalArgumentException();
        }
        return position == 0 ? boardSize - 1 : (position - 1) / boardSize;
    }

    private int getColumn(int position) {
        if (position < 0 || position >= boardSize * boardSize) {
            throw new IllegalArgumentException();
        }
        return position == 0 ? boardSize - 1 : (position - 1) % boardSize;
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(boardSize);
        sb.append("\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sb.append(boardTiles[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingDistance == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() == this.getClass()) {
            if (((Board) y).dimension() != dimension()) {
                return false;
            }

            if (((Board) y).boardTiles[emptyCellRow][emptyCellColumn] != boardTiles[emptyCellRow][emptyCellColumn]) {
                return false;
            }

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (((Board) y).boardTiles[i][j] != boardTiles[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean isInsideBoard(int positionRow, int positionColumn) {
        return 0 <= positionRow && 0 <= positionColumn && positionRow < boardSize && positionColumn < boardSize;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] directionRow = {-1, 0, 1, 0};
        int[] directionColumn = {0, 1, 0, -1};
        Stack<Board> boardNeighbors;
        boardNeighbors = new Stack<>();
        
        for (int direction = 0; direction < directionRow.length; direction++) {
            if (isInsideBoard(emptyCellRow + directionRow[direction], emptyCellColumn + directionColumn[direction])) {
                int[][] tmpBoard = new int[boardSize][boardSize];
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        tmpBoard[i][j] = boardTiles[i][j];
                    }
                }

                tmpBoard[emptyCellRow][emptyCellColumn] = tmpBoard[emptyCellRow + directionRow[direction]][emptyCellColumn + directionColumn[direction]];
                tmpBoard[emptyCellRow + directionRow[direction]][emptyCellColumn + directionColumn[direction]] = 0;
                boardNeighbors.push(new Board(tmpBoard));
            }
        }

        return boardNeighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                twinBoard[i][j] = boardTiles[i][j];
            }
        }
        if (twinBoard[0][0] == 0) {
            twinBoard[0][1] = boardTiles[1][0];
            twinBoard[1][0] = boardTiles[0][1];
        } else if (twinBoard[0][1] == 0) {
            twinBoard[0][0] = boardTiles[1][0];
            twinBoard[1][0] = boardTiles[0][0];
        } else {
            twinBoard[0][1] = boardTiles[0][0];
            twinBoard[0][0] = boardTiles[0][1];
        }
        return new Board(twinBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][]{
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}
        });

        StdOut.println("Dimention: " + board.dimension());
        StdOut.println("Hamming Distance: " + board.hamming());
        StdOut.println("Manhattan Distance: " + board.manhattan());
        StdOut.println("Is goal? " + board.isGoal());
        StdOut.println("Verify isGoal true: " + (new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        })));
        StdOut.println("Checking equals: " + board.equals(new Board(new int[][]{
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}
        })));
        StdOut.println("Checking equals: " + board.equals(new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        })));

        StdOut.println("Getting neighbor: ");
        for (Board b : board.neighbors()) {
            StdOut.println(b);
        }

        StdOut.println("Testing twin function: ");
        StdOut.println(new Board(new int[][]{
            {1, 0},
            {2, 3}
        }).twin());

        StdOut.println(new Board(new int[][]{
            {1, 0},
            {2, 3}
        }).twin());
    }
}