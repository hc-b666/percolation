import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double CONFIDENCE_95 = 1.96;
  private int n;
  private int trials;
  private double[] results;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

    this.n = n;
    this.trials = trials;
    this.results = new double[trials];

    int t = 0;
    while (t < this.trials) {
      Percolation p = new Percolation(this.n);
      while (!p.percolates()) {
        int r = StdRandom.uniformInt(this.n) + 1;
        int c = StdRandom.uniformInt(this.n) + 1;
        p.open(r, c);
      }
      this.results[t] = (double) p.numberOfOpenSites() / (this.n * this.n);
      t++;
    }
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(this.results);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(this.results);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("two ints are required for grid and trials");
    }

    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    PercolationStats ps = new PercolationStats(n, trials);
    System.out.println("mean = " + ps.mean());
    System.out.println("stddev = " + ps.stddev());
    System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
  }
}
