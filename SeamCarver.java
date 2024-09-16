import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {
    private int width; // width of pic
    private int height; // height of pic
    private Picture pic; // pic of the picture we are carving

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("null picture");
        }
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);

    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width()) {
            throw new IllegalArgumentException("column out of bounds");
        }

        if (y < 0 || y >= height()) {
            throw new IllegalArgumentException("row out of bounds");
        }
        // if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
        //     return 100000; // random high number

        return Math.sqrt(xGradientSquared(x, y) + yGradientSquared(x, y));
    }

    // this is the xGradientSquared helper method to calculate color differences
    private int xGradientSquared(int x, int y) {
        int leftRGB, rightRGB;
        if (x == 0) {
            leftRGB = pic.getRGB(width() - 1, y);  // Wrap around to the opposite edge
        }
        else {
            leftRGB = pic.getRGB(x - 1, y);
        }
        if (x == width() - 1) {
            rightRGB = pic.getRGB(0, y);  // Wrap around to the opposite edge
        }
        else {
            rightRGB = pic.getRGB(x + 1, y);
        }

        int rx = ((leftRGB >> 16) & 0xFF) - ((rightRGB >> 16) & 0xFF);
        int gx = ((leftRGB >> 8) & 0xFF) - ((rightRGB >> 8) & 0xFF);
        int bx = (leftRGB & 0xFF) - (rightRGB & 0xFF);

        return rx * rx + gx * gx + bx * bx;
    }

    // this is the yGradientSquared helper method to calculate color differences
    private int yGradientSquared(int x, int y) {
        int topRGB, bottomRGB;
        if (y == 0) {
            topRGB = pic.getRGB(x, height() - 1);
        }
        else {
            topRGB = pic.getRGB(x, y - 1);
        }
        if (y == height() - 1) {
            bottomRGB = pic.getRGB(x, 0);
        }
        else {
            bottomRGB = pic.getRGB(x, y + 1);
        }

        int ry = ((topRGB >> 16) & 0xFF) - ((bottomRGB >> 16) & 0xFF);
        int gy = ((topRGB >> 8) & 0xFF) - ((bottomRGB >> 8) & 0xFF);
        int by = (topRGB & 0xFF) - (bottomRGB & 0xFF);

        return ry * ry + gy * gy + by * by;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // Create energy grid
        double[][] energy = new double[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                energy[col][row] = energy(col, row);
            }
        }

        // Initialize DP tables for distances and edge tracking
        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                distTo[col][row] = Double.POSITIVE_INFINITY;
            }
            distTo[0][row] = energy[0][row];
        }

        // Fill DP tables
        for (int col = 0; col < width - 1; col++) {
            for (int row = 0; row < height; row++) {
                // Update right
                if (distTo[col + 1][row] > distTo[col][row] + energy[col + 1][row]) {
                    distTo[col + 1][row] = distTo[col][row] + energy[col + 1][row];
                    edgeTo[col + 1][row] = row;
                }
                // Update right and up
                if (row > 0 && distTo[col + 1][row - 1] > distTo[col][row]
                        + energy[col + 1][row - 1]) {
                    distTo[col + 1][row - 1] = distTo[col][row]
                            + energy[col + 1][row - 1];
                    edgeTo[col + 1][row - 1] = row;
                }
                // Update right and down
                if (row < height - 1 && distTo[col + 1][row + 1] >
                        distTo[col][row] + energy[col + 1][row + 1]) {
                    distTo[col + 1][row + 1] = distTo[col][row]
                            + energy[col + 1][row + 1];
                    edgeTo[col + 1][row + 1] = row;
                }
            }
        }

        // Find the minimum distance in the last column
        int minRow = 0;
        double minDist = Double.POSITIVE_INFINITY;
        for (int row = 0; row < height; row++) {
            if (distTo[width - 1][row] < minDist) {
                minDist = distTo[width - 1][row];
                minRow = row;
            }
        }

        // Trace back the minimum seam
        int[] horizontalSeam = new int[width];
        for (int col = width - 1; col >= 0; col--) {
            horizontalSeam[col] = minRow;
            minRow = edgeTo[col][minRow];
        }

        return horizontalSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energy[x][y] = energy(x, y);
            }
        }

        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
            distTo[x][0] = energy[x][0]; // Initialize first row of distTo
        }

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                if (x > 0) {
                    if (distTo[x - 1][y + 1] > distTo[x][y] + energy[x - 1][y + 1]) {
                        distTo[x - 1][y + 1] = distTo[x][y] + energy[x - 1][y + 1];
                        edgeTo[x - 1][y + 1] = x;
                    }
                }
                if (distTo[x][y + 1] > distTo[x][y] + energy[x][y + 1]) {
                    distTo[x][y + 1] = distTo[x][y] + energy[x][y + 1];
                    edgeTo[x][y + 1] = x;
                }
                if (x < width - 1) {
                    if (distTo[x + 1][y + 1] > distTo[x][y] + energy[x + 1][y + 1]) {
                        distTo[x + 1][y + 1] = distTo[x][y] + energy[x + 1][y + 1];
                        edgeTo[x + 1][y + 1] = x;
                    }
                }
            }
        }

        // Find the minimum energy column in the last row
        double minTotalEnergy = Double.POSITIVE_INFINITY;
        int minCol = 0;
        for (int x = 0; x < width; x++) {
            if (distTo[x][height - 1] < minTotalEnergy) {
                minTotalEnergy = distTo[x][height - 1];
                minCol = x;
            }
        }

        // Backtrack to find the vertical seam
        int[] seam = new int[height];
        for (int y = height - 1; y >= 0; y--) {
            seam[y] = minCol;
            minCol = edgeTo[minCol][y];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("seam is null");

        if (seam.length != width)
            throw new IllegalArgumentException("length of seam array does not "
                                                       + "equal width");

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("two adjacent indices don't "
                                                           + "differ by 1");
            }
        }

        Picture newPic = new Picture(width, height - 1);
        for (int col = 0; col < width; col++) {
            if (seam[col] < 0 || seam[col] >= height) {
                throw new IllegalArgumentException("row index is outside its range");
            }

            for (int row = 0, newRow = 0; row < height; row++) {
                if (row != seam[col]) {  // Copy all rows except the seam
                    newPic.setRGB(col, newRow, pic.getRGB(col, row));
                    newRow++; // Increment newRow only if the current row is not
                    // the seam
                }
            }
        }
        pic = newPic; // Update the picture reference
        height--; // Decrement the height of the picture
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("seam is null");

        if (seam.length != height) throw new
                IllegalArgumentException("length of seam array does not equal height");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new
                        IllegalArgumentException("two adjacent indices don't "
                                                         + "differ by 1");
            }
        }

        Picture pics = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {
                if (seam[row] < 0 || seam[row] > width - 1) {
                    throw new
                            IllegalArgumentException("colmun index is outside "
                                                             + "its range");
                }
                if (col < seam[row]) {
                    pics.setRGB(col, row, pic.getRGB(col, row));
                }
                else {
                    pics.setRGB(col, row, pic.getRGB(col + 1, row));
                }
            }
        }
        pic = pics;
        width--;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        // This test image is assumed to be a simple 5x5 image with arbitrary colors.

        Picture picture = new Picture("path_to_image.jpg");
        SeamCarver seamCarver = new SeamCarver(picture);

        // Call to picture() method to retrieve the current picture
        Picture currentPicture = seamCarver.picture();
        StdOut.println("Loaded picture dimensions: " + currentPicture.width() +
                               "x" + currentPicture.height());

        // Print initial dimensions
        StdOut.println(
                "Initial image dimensions: " + seamCarver.width() + "x"
                        + seamCarver.height());

        // Test finding seams
        int[] verticalSeam = seamCarver.findVerticalSeam();
        int[] horizontalSeam = seamCarver.findHorizontalSeam();

        StdOut.println("Vertical seam: " + Arrays.toString(verticalSeam));
        StdOut.println("Horizontal seam: " + Arrays.toString(horizontalSeam));

        // Test removing seams
        seamCarver.removeVerticalSeam(verticalSeam);
        seamCarver.removeHorizontalSeam(horizontalSeam);

        // Print dimensions after removing seams
        StdOut.println("Dimensions after removing one vertical and one "
                               + "horizontal seam: "
                               + seamCarver.width() + "x" + seamCarver.height());

        // Additional test - re-calculate energies and print some to verify changes
        StdOut.println("Re-check energy after seam removals at (1,1): "
                               + seamCarver.energy(1, 1));

    }
}


