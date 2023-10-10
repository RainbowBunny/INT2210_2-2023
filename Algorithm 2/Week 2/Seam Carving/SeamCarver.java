import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] distance;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        distance = new double[picture.width()][picture.height()];
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private double getSquaredColorDifference(Color x, Color y) {
        double diffRed = x.getRed() - y.getRed();
        double diffGreen = x.getGreen() - y.getGreen();
        double diffBlue = x.getBlue() - y.getBlue();
        return (diffRed * diffRed + diffGreen * diffGreen + diffBlue * diffBlue);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= picture.width() || y >= picture.height()) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || y == 0 || x == picture.width() - 1 || y == picture.height() - 1) {
            return 1000;
        } else {
            double deltaX = getSquaredColorDifference(picture.get(x - 1, y), picture.get(x + 1, y));
            double deltaY = getSquaredColorDifference(picture.get(x, y - 1), picture.get(x, y + 1));
            return Math.sqrt(deltaX + deltaY);
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // x inc
        int ans[] = new int[width()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (x == 0) {
                    distance[x][y] = 0;
                } else {
                    distance[x][y] = distance[x - 1][y];
                    if (y > 0) {
                        distance[x][y] = Math.min(distance[x][y], distance[x - 1][y - 1]);
                    } 
                    if (y + 1 < height()) {
                        distance[x][y] = Math.min(distance[x][y], distance[x - 1][y + 1]);
                    }
                }
                distance[x][y] += energy(x, y);
            }
        }

        for (int y = 0; y < height(); y++) {
            if (distance[width() - 1][y] < distance[width() - 1][ans[width() - 1]]) {
                ans[width() - 1] = y;
            }    
        }

        for (int x = width() - 2; x >= 0; x--) {
            ans[x] = ans[x + 1];
            for (int j = -1; j <= 1; j++) {
                int y = ans[x + 1] + j;
                if (y < 0 || y >= height()) {
                    continue;
                }
                if (distance[x][y] + energy(x + 1, ans[x + 1]) == distance[x + 1][ans[x + 1]]) {
                    ans[x] = y;
                }
            }
        }
        return ans;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // y inc
        int ans[] = new int[height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (y == 0) {
                    distance[x][y] = 0;
                } else {
                    distance[x][y] = distance[x][y - 1];
                    if (x > 0) {
                        distance[x][y] = Math.min(distance[x][y], distance[x - 1][y - 1]);
                    } 
                    if (x + 1 < width()) {
                        distance[x][y] = Math.min(distance[x][y], distance[x + 1][y - 1]);
                    }
                }
                distance[x][y] += energy(x, y);
            }
        }

        for (int x = 0; x < width(); x++) {
            if (distance[x][height() - 1] < distance[ans[height() - 1]][height() - 1]) {
                ans[height() - 1] = x;
            }    
        }

        for (int y = height() - 2; y >= 0; y--) {
            ans[y] = ans[y + 1];
            for (int j = -1; j <= 1; j++) {
                int x = ans[y + 1] + j;
                if (x < 0 || x >= width()) {
                    continue;
                }
                if (distance[x][y] + energy(ans[y + 1], y + 1) == distance[ans[y + 1]][y + 1]) {
                    ans[y] = x;
                }
            }
        }
        return ans;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (i + 1 < seam.length) {
                if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
            if (seam[i] < 0 || seam[i] >= height()) {
                throw new IllegalArgumentException();
            }
        }

        Picture newPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (seam[x] == y) {
                    continue;
                } else if (seam[x] > y) {
                    newPicture.set(x, y, picture.get(x, y));
                } else {
                    newPicture.set(x, y - 1, picture.get(x, y));
                }
            }
        }
        picture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (i + 1 < seam.length) {
                if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IllegalArgumentException();
            }
        }

        Picture newPicture = new Picture(width() - 1, height());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (seam[y] == x) {
                    continue;
                } else if (seam[y] > x) {
                    newPicture.set(x, y, picture.get(x, y));
                } else {
                    newPicture.set(x - 1, y, picture.get(x, y));
                }
            }
        }
        picture = newPicture;
    }
}