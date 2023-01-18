import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class SeamCarver {

    private Picture picture; // copy of given picture
    private int width; // width of picture
    private int height; // width of picture


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture cannot be null");
        }

        width = picture.width();
        height = picture.height();

        this.picture = new Picture(width, height);

        // defensively copy given picture
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int color = picture.getRGB(col, row);
                this.picture.setRGB(col, row, color);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture current = new Picture(width, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int color = picture.getRGB(col, row);
                current.setRGB(col, row, color);
            }
        }
        return current;
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
        // catch illegal arguments
        if (x < 0 || x >= width) {
            throw new IllegalArgumentException("Column is outside prescribed range");
        }
        if (y < 0 || y >= height) {
            throw new IllegalArgumentException("Row is outside prescribed range");
        }

        // calculate x-gradient
        int x1Val = x - 1;
        if (x == 0) { // if current pixel is in left-most column
            x1Val = width - 1; // x-value of left-adjacent = right-most column
        }

        // RGB values of left-adjacent pixel
        int rgbLeft = picture.getRGB(x1Val, y);
        int rLeft = (rgbLeft >> 16) & 0xFF;
        int gLeft = (rgbLeft >> 8) & 0xFF;
        int bLeft = rgbLeft & 0xFF;

        int x2Val = x + 1;
        if (x == width - 1) { // if current pixel is in right-most column
            x2Val = 0; // x-value of right-adjacent = left-most column
        }

        // RGB values of right-adjacent pixel
        int rgbRight = picture.getRGB(x2Val, y);
        int rRight = (rgbRight >> 16) & 0xFF;
        int gRight = (rgbRight >> 8) & 0xFF;
        int bRight = rgbRight & 0xFF;

        // square of x-gradient
        double xGrad = Math.pow((rLeft - rRight), 2.0) + Math.pow((gLeft - gRight), 2) +
                Math.pow((bLeft - bRight), 2);

        // calculate y-gradient
        int y1Val = y - 1; // y-value of top-adjacent pixel
        if (y == 0) { // if current pixel is in top-most column
            y1Val = height - 1; // y-value of top-adjacent = bottom-most column
        }

        // RGB values of top-adjacent pixel
        int rgbTop = picture.getRGB(x, y1Val);
        int rTop = (rgbTop >> 16) & 0xFF;
        int gTop = (rgbTop >> 8) & 0xFF;
        int bTop = rgbTop & 0xFF;

        int y2Val = y + 1;
        if (y == height - 1) { // if current pixel is in bottom-most column
            y2Val = 0; // y-value of bottom-adjacent = top-most column
        }

        // RGB values of bottom-adjacent pixel
        int rgbBot = picture.getRGB(x, y2Val);
        int rBot = (rgbBot >> 16) & 0xFF;
        int gBot = (rgbBot >> 8) & 0xFF;
        int bBot = rgbBot & 0xFF;

        // square of x-gradient
        double yGrad = Math.pow((rTop - rBot), 2.0) + Math.pow((gTop - gBot), 2) +
                Math.pow((bTop - bBot), 2);

        return Math.sqrt(xGrad + yGrad);
    }


    // returns matrix of energies
    private double[][] energyGraph() {
        double[][] graph = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                graph[i][j] = energy(i, j);
            }
        }
        return graph;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // transpose and find vertical seam
        transpose(picture);
        int[] shortest = findVerticalSeam();
        // reset picture orientation
        transpose(picture);
        return shortest;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] graph = energyGraph();
        int[] path = findShort(graph);
        return path;
    }

    // implement bellman ford’s algorithm for lowest-energy seam
    private int[] findShort(double[][] graph) {
        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        // fill distto w initial vals
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // top row dist is the energies
                if (j == 0) distTo[i][j] = graph[i][j];
                    // all else is inf at first
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // iterate through all points, relaxing adj each time if lower-energy
        // path found
        //  adapted from ShortestPaths slide 20, bellman-ford
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {

                // if old distTo> origin point dist + energy, update distTo
                double e = graph[j][i + 1];
                double dist = distTo[j][i] + e;

                // check bottom adj
                if (dist < distTo[j][i + 1]) {
                    edgeTo[j][i + 1] = j;
                    distTo[j][i + 1] = dist;
                }

                // check bottom right adj
                // if col is last, don't check to right
                if (j < (width - 1)) {
                    e = graph[j + 1][i + 1];
                    dist = distTo[j][i] + e;
                    if (dist < distTo[j + 1][i + 1]) {
                        edgeTo[j + 1][i + 1] = j;
                        distTo[j + 1][i + 1] = dist;

                    }
                }
                // check bottom left adj
                // if col is 0, don't check to left
                if (j >= 1) {
                    e = graph[j - 1][i + 1];
                    dist = distTo[j][i] + e;
                    if (dist < distTo[j - 1][i + 1]) {
                        edgeTo[j - 1][i + 1] = j;
                        distTo[j - 1][i + 1] = dist;
                    }
                }
            }
        }

        // find the lowest energy on the bottom row
        int bottomRow = height - 1;
        double minDist = Double.POSITIVE_INFINITY;
        IndexMinPQ<Double> minq = new IndexMinPQ<Double>(width);
        for (int i = 0; i < width; i++) {
            minq.insert(i, distTo[i][bottomRow]);
        }
        int colMin = minq.delMin();


        // trace that min value up in edgeTo to get the shortest path
        int[] shortest = new int[height];
        for (int i = height - 1; i >= 0; i--) {
            shortest[i] = colMin;
            colMin = edgeTo[colMin][i];
        }

        return shortest;
    }


    // transpose picture
    private void transpose(Picture transpose) {
        Picture transposed = new Picture(height(), width());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = transpose.getRGB(i, j);
                transposed.setRGB(j, i, color);
            }
        }

        int oldW = width;

        this.width = height();
        this.height = oldW;

        this.picture = transposed;
    }

    // check that seam entries are within the picture's width bounds
    private void validateHorizontal(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Null horizontal seam");
        }
        if (seam.length != width) {
            throw new IllegalArgumentException("Horizontal seam out of bounds");
        }
        // check that each element in the seam is a valid row/col #
        for (int i = 0; i < seam.length; i++) {

            // check that seam is in bounds of 0 and length-1
            if (seam[i] < 0) {
                throw new IllegalArgumentException("element out of bounds");
            }

            if (seam[i] >= height) {
                throw new IllegalArgumentException("element out of bounds");
            }
            // if element isn't first, check before
            if (i != 0) {

                int dif = seam[i] - seam[i - 1];
                if (dif != 0 && dif != 1 && dif != -1) {
                    throw new IllegalArgumentException("argument must be a seam.");
                }
            }

            // if element isn't last, check after
            if (i != seam.length - 1) {
                int dif = seam[i] - seam[i + 1];
                if (dif != 0 && dif != 1 && dif != -1) {
                    throw new IllegalArgumentException("argument must be a seam.");
                }
            }

        }

    }

    // check that seam entries are within the picture's height bounds
    private void validateVertical(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Null vertical seam");
        }
        if (seam.length != height) {
            throw new IllegalArgumentException("Vertical seam out of bounds");
        }
        // check that each element in the seam is a valid row/col #
        for (int i = 0; i < seam.length; i++) {

            // check that seam is in bounds of 0 and length-1
            if (seam[i] < 0) {
                throw new IllegalArgumentException("element out of bounds");
            }

            if (seam[i] >= width) {
                throw new IllegalArgumentException("element out of bounds");
            }
            // if element isn't first, check before
            if (i != 0) {

                int dif = seam[i] - seam[i - 1];
                if (dif != 0 && dif != 1 && dif != -1) {
                    throw new IllegalArgumentException("argument must be a seam.");
                }
            }

            // if element isn't last, check after
            if (i != seam.length - 1) {
                int dif = seam[i] - seam[i + 1];
                if (dif != 0 && dif != 1 && dif != -1) {
                    throw new IllegalArgumentException("argument must be a seam.");
                }
            }

        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateHorizontal(seam);
        transpose(picture);
        removeVerticalSeam(seam);

        // reset picture orientation
        transpose(picture);

    }


    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateVertical(seam);
        Picture tempPic = new Picture(width - 1, height);

        // copy picture into new picture, skip deleted seam
        for (int i = 0; i < height(); i++) {
            int delete = seam[i];
            for (int j = 0; j < width - 1; j++) {
                if (j < delete) {
                    int rgb = this.picture.getRGB(j, i);
                    tempPic.setRGB(j, i, rgb);
                }
                else {
                    int rgb = this.picture.getRGB(j + 1, i);
                    tempPic.setRGB(j, i, rgb);
                }
            }
        }
        // adjust width dimension
        width--;
        this.picture = tempPic;
    }


    //  unit testing (required)
    public static void main(String[] args) {
        Picture edit = new Picture(args[0]);
        SeamCarver test = new SeamCarver(edit);

        test.picture().show();
        StdOut.println(test.height());
        StdOut.println(test.width());

        StdOut.println(test.energy(3, 4));

        Stopwatch time = new Stopwatch();
        int[] seam = test.findVerticalSeam();
        test.removeVerticalSeam(seam);
        int[] hSeam = test.findHorizontalSeam();
        test.removeHorizontalSeam(hSeam);
        StdOut.println("Elapsed Time: " + time.elapsedTime());


    }


}










