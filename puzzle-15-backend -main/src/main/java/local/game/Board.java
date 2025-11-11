package local.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Board {
    private static final int N = 4;
    private final int[][] tiles = new int[N][N];

    private Board() {
        int k = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = (i == N - 1 && j == N - 1) ? 0 : k++;
            }
        }
    }

    public static Board solved() { return new Board(); }
    public int size() { return N; }
    public int tileAt(int row, int col) {
        checkBounds(row, col);
        return tiles[row][col];
    }

    public int[][] snapshot() {
        int[][] c = new int[N][N];
        for (int i = 0; i < N; i++) System.arraycopy(tiles[i],
                0, c[i], 0, N);
        return c;
    }

    public boolean canSlideTile(int tileValue) {
        int[] z = find(0);
        int[] t = find(tileValue);
        if (z == null || t == null) return false;
        return manhattan(z, t) == 1;
    }

    public boolean slideTile(int tileValue) {
        if (!canSlideTile(tileValue)) return false;
        int[] z = find(0);
        int[] t = find(tileValue);
        swap(z[0], z[1], t[0], t[1]);
        return true;
    }

    public boolean canSlideAt(int row, int col) {
        checkBounds(row, col);
        int[] z = find(0);
        int[] t = new int[]{row, col};
        return manhattan(z, t) == 1;
    }

    public boolean slideAt(int row, int col) {
        if (!canSlideAt(row, col)) return false;
        int[] z = find(0);
        swap(z[0], z[1], row, col);
        return true;
    }

    public boolean canSlide(Direction dir) {
        int[] z = find(0);
        int tr = switch (dir) {
            case UP    -> z[0] + 1;
            case DOWN  -> z[0] - 1;
            case LEFT  -> z[1] + 1;
            case RIGHT -> z[1] - 1;
        };
        int tc = switch (dir) {
            case UP, DOWN -> z[1];
            case LEFT, RIGHT -> z[1];
        };
        return inBounds(tr, tc);
    }

    public boolean slide(Direction dir) {
        if (!canSlide(dir)) return false;
        int[] z = find(0);
        int tr = switch (dir) {
            case UP    -> z[0] + 1;
            case DOWN  -> z[0] - 1;
            case LEFT  -> z[1] + 1;
            case RIGHT -> z[1] - 1;
        };

        int zr = z[0], zc = z[1];
        int trr = zr, tcc = zc;
        switch (dir) {
            case UP    -> trr = zr + 1;
            case DOWN  -> trr = zr - 1;
            case LEFT  -> tcc = zc + 1;
            case RIGHT -> tcc = zc - 1;
        }
        swap(zr, zc, trr, tcc);
        return true;
    }

    public boolean isOrdered() {
        int want = 1;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1) return tiles[i][j] == 0;
                if (tiles[i][j] != want++) return false;
            }
        return true;
    }

    public void shuffleByRandomMoves(int moves, Random rnd) {
        for (int m = 0; m < moves; m++) {
            int[] z = find(0);
            List<int[]> neigh = new ArrayList<>(4);
            if (z[0] > 0) neigh.add(new int[]{z[0] - 1, z[1]});
            if (z[0] < N - 1) neigh.add(new int[]{z[0] + 1, z[1]});
            if (z[1] > 0) neigh.add(new int[]{z[0], z[1] - 1});
            if (z[1] < N - 1) neigh.add(new int[]{z[0], z[1] + 1});
            int[] t = neigh.get(rnd.nextInt(neigh.size()));
            swap(z[0], z[1], t[0], t[1]);
        }
    }

    private int[] find(int v) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] == v) return new int[]{i, j};
        return null;
    }

    private static int manhattan(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }

    private static boolean inBounds(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
    }

    private static void checkBounds(int r, int c) {
        if (!inBounds(r, c)) {
            throw new IllegalArgumentException("Out of bounds: (" + r + "," + c + ")");
        }
    }

    private void swap(int r1, int c1, int r2, int c2) {
        int tmp = tiles[r1][c1];
        tiles[r1][c1] = tiles[r2][c2];
        tiles[r2][c2] = tmp;
    }
}
