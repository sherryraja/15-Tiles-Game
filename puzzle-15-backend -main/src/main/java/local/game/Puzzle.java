package local.game;

public interface Puzzle {
    boolean moveTile(int tileValue);
    boolean isSolved();
    boolean hasLost();
    int movesMade();
    int maxMoves();
    int[][] snapshot();

    default boolean moveTile(int row, int col) {
        int[][] s = snapshot();
        if (s == null || s.length == 0) return false;
        if (row < 0 || col < 0 || row >= s.length || col >= s[0].length) return false;

        int v = s[row][col];
        return v != 0 && moveTile(v);
    }
    default boolean moveTile(Direction dir) {
        int[][] s = snapshot();
        if (s == null || s.length == 0) return false;

        int zr = -1, zc = -1;
        outer:
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length; j++) {
                if (s[i][j] == 0) { zr = i; zc = j; break outer; }
            }
        }
        if (zr == -1) return false;

        int tr = zr, tc = zc;
        switch (dir) {
            case UP:    tr = zr + 1; tc = zc;     break;
            case DOWN:  tr = zr - 1; tc = zc;     break;
            case LEFT:  tr = zr;     tc = zc + 1; break;
            case RIGHT: tr = zr;     tc = zc - 1; break;
        }
        if (tr < 0 || tc < 0 || tr >= s.length || tc >= s[0].length) return false;
        int v = s[tr][tc];
        return v != 0 && moveTile(v);
    }
}
