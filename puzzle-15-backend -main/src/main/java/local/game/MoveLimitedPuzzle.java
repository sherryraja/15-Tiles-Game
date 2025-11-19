package local.game;

public class MoveLimitedPuzzle implements Puzzle {

    private final Board board;
    private int moveCount = 0;
    private final int moveLimit;

    public MoveLimitedPuzzle(Board board, int moveLimit) {
        this.board = board;
        this.moveLimit = moveLimit;
    }
    @Override
    public boolean moveTile(int tileValue) {
        if (isSolved() || hasLost()) return false;
        boolean ok = board.slideTile(tileValue);
        if (ok) moveCount++;
        return ok;
    }
    protected boolean hasLostCondition() {
        return moveCount >= moveLimit;
    }

    @Override public boolean hasLost()  { return hasLostCondition(); }
    @Override public boolean isSolved() { return board.isOrdered(); }
    @Override public int movesMade()    { return moveCount; }

    @Override public int maxMoves()     { return moveLimit; }
    @Override public int[][] snapshot() { return board.snapshot(); }

    protected Board board() { return board; }

    protected int moveCount() { return moveCount; }

    protected int moveLimit() { return moveLimit; }
}
