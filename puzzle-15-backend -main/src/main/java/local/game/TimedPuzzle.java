package local.game;

import java.time.Duration;
import java.time.Instant;

public class TimedPuzzle extends MoveLimitedPuzzle {

    private final Duration timeLimit;
    private final Instant startedAt;

    public TimedPuzzle(Board board, int moveLimit, Duration timeLimit) {
        super(board, moveLimit);
        this.timeLimit = timeLimit;
        this.startedAt = Instant.now();
    }

    private Duration elapsed() {
        return Duration.between(startedAt, Instant.now());
    }

    @Override
    protected boolean hasLostCondition() {
        boolean moveLimitHit = super.hasLostCondition();
        boolean timeOver = elapsed().compareTo(timeLimit) >= 0;
        return moveLimitHit || timeOver;
    }

    public Duration getTimeLimit() { return timeLimit; }
    public Duration getElapsed()   { return elapsed(); }
}
