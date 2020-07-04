import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

//java Percolation
public class Percolation {
    private final WeightedQuickUnionUF wQF;
    private final WeightedQuickUnionUF fullCheck;
    private boolean[] sites;
    private final int n;
    private final int size;
    private int openSites;

    //creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        checkSize(n);
        this.n = n;
        //Size of array including Space for two extra top and bottom virtual nodes
        this.size = this.n * this.n + 2;
        //Data structure to store the nodes
        this.wQF = new WeightedQuickUnionUF(size);
        
        //WQU for storing the nodes which are percolating and are full as well
        this.fullCheck = new WeightedQuickUnionUF(size);
        
        this.sites = new boolean[size];
        this.openSites = 0;
    }

    //opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRowCol(row, col);
        int idx = calIndex(row, col);
        if (this.sites[idx] == false) {
            connectVirtualNodes(row, col);
            connectAdjacent(row, col);
            this.sites[idx] = true;
            this.openSites++;
        }
    }

    //To connect adjacent nodes, only if open
    private void connectAdjacent(int row, int col) {
        int rowCount = row - 1;
        while (rowCount <= row + 1) {
            int colCount = col;
            if (row == rowCount) {
                colCount = col - 1;
                while (colCount <= col + 1) {
                    if (rowCount > 0 && rowCount <= this.n && colCount > 0 && colCount <= this.n) {
                        if (isOpen(rowCount, colCount)) {
                            wQF.union(calIndex(rowCount, colCount), calIndex(row, col));
                            fullCheck.union(calIndex(rowCount, colCount), calIndex(row, col));
                            if (isFull(rowCount, colCount)) sites[calIndex(row, col)] = true;
                        }
                    }
                    colCount += 2;
                }
            } else {
                if (rowCount > 0 && rowCount <= this.n && colCount > 0 && colCount <= this.n) {

                    if (isOpen(rowCount, colCount)) {
                        wQF.union(calIndex(rowCount, colCount), calIndex(row, col));
                        fullCheck.union(calIndex(rowCount, colCount), calIndex(row, col));
                        if (isFull(rowCount, colCount)) sites[calIndex(row, col)] = true;
                    }
                }
            }

            rowCount += 1;
        }
    }

    //Calculate the array index based on row and column value
    private int calIndex(int row, int col) {
        return (this.n * (row - 1)) + (col - 1);
    }

    //Connect the node at (row, col) to the virtual top or bottom node
    private void connectVirtualNodes(int row, int col) {
        if (row == 1) {
            int idx = calIndex(row, col);
            wQF.union(idx, size - 2);
            fullCheck.union(idx, size - 2);

        } else if (row == this.n) {
            int idx = calIndex(row, col);
            fullCheck.union(idx, size - 1);

        }
    }

    //is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);
        return sites[calIndex(row, col)] != false;
    }

    //is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowCol(row, col);
        return wQF.find(size - 2) == wQF.find(calIndex(row, col));
    }

    //returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    //does the system percolate?
    public boolean percolates() {

        if (size <= 3 && sites[0]) return true;
        return fullCheck.find(size - 2) == fullCheck.find(size - 1);
    }

    //Validation
    private void checkRowCol(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.n || col > this.n) throw new IllegalArgumentException();
    }

    private void checkSize(int n) {
        if (n <= 0) throw new IllegalArgumentException();
    }

    //test client (optional)
    public static void main(String[] args) {
        
        //Tester to test if sample matrix of 4*4 is percolating.
        Percolation perc = new Percolation(4);
        perc.open(1, 2);
        StdOut.println("Isfull= " + perc.isFull(1, 2) + " open sites=" + perc.numberOfOpenSites());
        StdOut.println("IsOpen= " + perc.isOpen(2, 2));
        perc.open(2, 2);
        perc.open(2, 3);
        StdOut.println("IsFull= " + perc.isFull(2, 3));
        perc.open(3, 3);
        perc.open(3, 4);
        StdOut.println("IsFull= " + perc.isFull(3, 4));
        StdOut.println("Percolates?= " + perc.percolates());
        perc.open(4, 3);
        StdOut.println("Percolates4,3?= " + perc.percolates());
        perc.open(4, 4);
        StdOut.println("IsFull4,4= " + perc.isFull(4, 4));
        StdOut.println("Percolates?= " + perc.percolates());
    }
}
