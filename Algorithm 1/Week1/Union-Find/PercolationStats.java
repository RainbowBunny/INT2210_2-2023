import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private int gridSize, trials;
    private double[] sample;
    static private double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        gridSize = n;
        this.trials = trials;

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        sample = new double[trials];
        
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            int[] permutation = new int[n * n];
            for (int j = 0; j < permutation.length; j++) {
                permutation[j] = j;
            }
            StdRandom.shuffle(permutation);
            for (int j = 0; j < permutation.length; j++) {
                int posX = permutation[j] / n + 1;
                int posY = permutation[j] % n + 1;
                percolation.open(posX, posY);
                if (percolation.percolates()) {
                    sample[i] = 1.0 * (j + 1) / (n * n);
                    break;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(sample);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(sample);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / java.lang.Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / java.lang.Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}
