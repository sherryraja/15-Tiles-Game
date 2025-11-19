import local.services.GameManagerService;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

    @Test
    public void testGenerateGameId() {
        GameManagerService gameManagerService = new GameManagerService();

        Set<String> gameIds = new HashSet<>();
        int numberOfIdsToGenerate = 10000;
        for (int i = 0; i < numberOfIdsToGenerate; i++) {
            String gameId = gameManagerService.generateGameId();
            assertNotNull(gameId);
            assertFalse(gameIds.contains(gameId), "Duplicate game ID generated: " + gameId);
            gameIds.add(gameId);
        }
        assertEquals(numberOfIdsToGenerate, gameIds.size());
    }
}
