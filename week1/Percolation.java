import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid;
    private int openSites;
    private final int gridSize;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private int virtualTop;
    private int virtualBottom;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        gridSize = n;
        int siteNum = n * n + 2;
        grid = new boolean[siteNum];
        openSites = 0;
        uf = new WeightedQuickUnionUF(siteNum);
        ufFull = new WeightedQuickUnionUF(siteNum);
        virtualTop = gridSize * gridSize;
        virtualBottom = gridSize * gridSize + 1;

        // init grid
        for (int i = 0; i < gridSize * gridSize; i++) {
            grid[i] = false;
        }

        grid[virtualTop] = true;
        grid[virtualBottom] = true;

        // connect top and bottom sites to virtual top and bottom
        for (int i = 0; i < gridSize; i++) {
            uf.union(i, virtualTop);
            uf.union(gridSize * (gridSize - 1) + i, virtualBottom);
            ufFull.union(i, virtualTop);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }

        row -= 1;
        col -= 1;
        grid[row * gridSize + col] = true;
        openSites++;

        if (row > 0 && isOpen(row, col + 1)) {
            uf.union(row * gridSize + col, (row - 1) * gridSize + col);
            ufFull.union(row * gridSize + col, (row - 1) * gridSize + col);
        }

        if (row < gridSize - 1 && isOpen(row + 2, col + 1)) {
            uf.union(row * gridSize + col, (row + 1) * gridSize + col);
            ufFull.union(row * gridSize + col, (row + 1) * gridSize + col);
        }

        if (col > 1 && isOpen(row + 1, col)) {
            uf.union(row * gridSize + col, row * gridSize + col - 1);
            ufFull.union(row * gridSize + col, row * gridSize + col - 1);
        }

        if (col < gridSize && isOpen(row + 1, col + 2)) {
            uf.union(row * gridSize + col, row * gridSize + col + 1);
            ufFull.union(row * gridSize + col, row * gridSize + col + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIds(row, col);
        return grid[(row - 1) * gridSize + col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIds(row, col);
        return ufFull.find(virtualTop) ==
                ufFull.find((row - 1) * gridSize + col - 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (gridSize == 1)
            return grid[0];

        return uf.find(virtualTop) == uf.find(virtualBottom);
    }

    // validates grid bounds
    private void validateIds(int row, int col) {
        if (row > gridSize || row < 1 || col > gridSize || col < 1)
            throw new IllegalArgumentException("Row or column indexes are not correct!");
    }
}
