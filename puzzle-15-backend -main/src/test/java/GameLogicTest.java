import local.services.GameLogicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {
    private GameLogicService gameLogicService;

    @BeforeEach
    public void setUp() {
        gameLogicService = new GameLogicService(4);
        gameLogicService.shuffleGame();
    }

    @Test
    public void testGameState() {
        int[][] gameState = gameLogicService.getGameState();
        assertNotNull(gameState);
        assertEquals(4, gameState.length);
        assertEquals(4, gameState[0].length);
        assertEquals(4, gameState[1].length);
        assertEquals(4, gameState[2].length);
        assertEquals(4, gameState[3].length);
    }

    @Test
    public void testNumberRange() {
        int[][] gameState = gameLogicService.getGameState();
        for (int[] row : gameState) {
            for (int tile : row) {
                assertTrue(tile >= 0 && tile <= 15);
            }
        }
    }
}
