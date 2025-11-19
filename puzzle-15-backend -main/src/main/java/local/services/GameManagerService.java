package local.services;

import local.game.Board;
import local.game.Puzzle;
import local.game.TimedPuzzle;
import local.game.Direction;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class GameManagerService {

    private static final int MOVE_LIMIT = 250;
    private static final Duration TIME_LIMIT = Duration.ofMinutes(3);

    private final Map<String, Puzzle> games = new ConcurrentHashMap<>();
    private final Random rnd = new Random();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Map<String, ScheduledFuture<?>> timeouts = new ConcurrentHashMap<>();
    private final Set<String> timedOut = ConcurrentHashMap.newKeySet();

    public String createGame() {
        String id = UUID.randomUUID().toString();

        Board board = Board.solved();
        board.shuffleByRandomMoves(80, rnd);

        TimedPuzzle timed = new TimedPuzzle(board, MOVE_LIMIT, TIME_LIMIT);
        Puzzle game = timed;
        games.put(id, game);

        long seconds = TIME_LIMIT.toSeconds();
        ScheduledFuture<?> f = scheduler.schedule(() -> timedOut.add(id),
                seconds, TimeUnit.SECONDS);
        timeouts.put(id, f);
        return id;
    }
    public int[][] getGameState(String gameId) {
        Puzzle g = games.get(gameId);
        return g == null ? null : g.snapshot();
    }
    public void makeMove(String gameId, int tileValue) {
        Puzzle g = requireGame(gameId);
        if (timedOut.contains(gameId) || g.hasLost() || g.isSolved()) return;
        g.moveTile(tileValue);
        if (g.isSolved()) cancelTimeout(gameId);
    }

    public void makeMove(String gameId, int row, int col) {
        Puzzle g = requireGame(gameId);
        if (timedOut.contains(gameId) || g.hasLost() || g.isSolved()) return;
        g.moveTile(row, col);
        if (g.isSolved()) cancelTimeout(gameId);
    }

    public void makeMove(String gameId, Direction dir) {
        Puzzle g = requireGame(gameId);
        if (timedOut.contains(gameId) || g.hasLost() || g.isSolved()) return;
        g.moveTile(dir);
        if (g.isSolved()) cancelTimeout(gameId);
    }

    public boolean isGameComplete(String gameId) {
        Puzzle g = games.get(gameId);
        return g != null && g.isSolved();
    }

    public boolean isGameLost(String gameId) {
        Puzzle g = games.get(gameId);
        return g != null && (timedOut.contains(gameId) || g.hasLost());
    }

    public int getMovesMade(String gameId) {
        Puzzle g = games.get(gameId);
        return g == null ? 0 : g.movesMade();
    }

    public int getMaxMoves(String gameId) {
        Puzzle g = games.get(gameId);
        return g == null ? 0 : g.maxMoves();
    }

    private Puzzle requireGame(String gameId) {
        Puzzle g = games.get(gameId);
        if (g == null) throw new IllegalArgumentException("Game not found: " + gameId);
        return g;
    }

    private void cancelTimeout(String gameId) {
        ScheduledFuture<?> f = timeouts.remove(gameId);
        if (f != null) f.cancel(true);
        timedOut.remove(gameId);
    }

    @PreDestroy
    public void shutdown() {

        timeouts.values().forEach(f -> { if (f != null) f.cancel(true); });
        scheduler.shutdownNow();
    }

    public String generateGameId() {
        return "";
    }
}
