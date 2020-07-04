import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] trialsResult;
    private final int trials;
    private final double z;

    //perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        checkInitialization(n, trials);
        this.trials = trials;
        trialsResult = new double[trials];
        this.z = 1.96;
        for (int i = 0; i < trials; i++) {
            trialsResult[i] = performTrials(n);
        }
    }

    //Perform trials for the percolation
    private double performTrials(int n) {
        int size = n * n;
        Percolation perc = new Percolation(n);
        while (!perc.percolates()) {
            int row = genRandom(n);
            int col = genRandom(n);
            //StdOut.println("row, col=" + row + ", " + col);
            if (!perc.isOpen(row, col)) {
                perc.open(row, col);
            }
        }
        return ((double) perc.numberOfOpenSites() / (double) size);
    }

    private void checkInitialization(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
    }

    private int genRandom(int n) {
        return StdRandom.uniform(1, n + 1);
    }

    //sample mean of percolation threshold
    public double mean() {

        return StdStats.mean(trialsResult);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialsResult);
    }

    //low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (z * (stddev() / Math.sqrt(trials)));
    }

    //high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (z * (stddev() / Math.sqrt(trials)));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats prcs = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + prcs.mean());
        StdOut.println("stddev                  = " + prcs.stddev());
        StdOut.println("95% confidence interval = [" +
                prcs.confidenceHi() + ", " + prcs.confidenceLo() + "]");
    }
}
