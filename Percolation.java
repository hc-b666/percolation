import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private int n;
  private int open;
  private boolean[][] grid;
  private WeightedQuickUnionUF uf;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }

    this.n = n;
    this.open = 0;
    this.grid = new boolean[n + 1][n + 1];
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        this.grid[i][j] = false;
      }
    }
    this.uf = new WeightedQuickUnionUF(n * n + 2);
  }

  // validate the row and col
  private void validate(int row, int col) {
    if (row <= 0 || row > this.n)
      throw new IllegalArgumentException();
    if (col <= 0 || col > this.n)
      throw new IllegalArgumentException();
  }

  // index = (row - 1) * n + (col - 1) in union find structure
  private int ufIndex(int row, int col) {
    return (row - 1) * this.n + col - 1;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    validate(row, col);

    if (!isOpen(row, col)) {
      grid[row][col] = true;
      open++;

      int site = ufIndex(row, col);

      // union(site, top)
      if (row > 1 && grid[row - 1][col]) {
        int top = ufIndex(row - 1, col);
        uf.union(site, top);
      }

      // union(site, bottom)
      if (row < this.n && grid[row + 1][col]) {
        int bottom = ufIndex(row + 1, col);
        uf.union(site, bottom);
      }

      // union(site, left)
      if (col > 1 && grid[row][col - 1]) {
        int left = ufIndex(row, col - 1);
        uf.union(site, left);
      }

      // union(site, right)
      if (col < this.n && grid[row][col + 1]) {
        int right = ufIndex(row, col + 1);
        uf.union(site, right);
      }

      // if site is in top row connect it to virtual top site
      if (row == 1) {
        uf.union(0, site);
      }

      // if site is in bottom row connect it to virtual bottom site
      if (row == this.n) {
        uf.union(n * n + 1, site);
      }
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    validate(row, col);
    return grid[row][col];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    validate(row, col);
    if (!isOpen(row, col))
      return false;
    int site = ufIndex(row, col);
    return uf.find(site) == uf.find(0);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return open;
  }

  // does the system percolate?
  public boolean percolates() {
    return uf.find(0) == uf.find(n * n + 1);
  }
}
