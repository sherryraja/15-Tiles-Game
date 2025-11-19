package local.services;

import java.util.Arrays;

public class GameLogicService {
    private int[][] gameState;
    private final int size;

    public GameLogicService(int size) {
        this.size = size;
        initializeGameState();
    }

    /**
     * Initialize game state
     */
    private void initializeGameState() {
        gameState = new int[size][size];
        int count = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gameState[i][j] = count++;
            }
        }
        gameState[size - 1][size - 1] = 0;
    }

    public int[][] getGameState() {
        return gameState;
    }


    public void makeMove(int tileValue) {
        int[] tilePosition = findTilePosition(tileValue);
        int[] emptyTilePosition = findEmptyTilePosition();

        if (tilePosition == null || emptyTilePosition == null) {
            throw new IllegalArgumentException("Invalid move");
        }

        if (!isAdjacent(tilePosition, emptyTilePosition)) {
            throw new IllegalArgumentException("Invalid move");
        }

        swapTiles(tilePosition, emptyTilePosition);
    }

    public void shuffleGame() {
        for (int i = 0; i < 1000; i++) {
            int[] emptyTilePosition = findEmptyTilePosition();
            int randomDirection = (int) (Math.random() * 4);
            int[] newPosition = getNewPosition(emptyTilePosition, randomDirection);
            if (isValidPosition(newPosition)) {
                swapTiles(emptyTilePosition, newPosition);
            }
        }
    }

    public boolean isGameComplete() {
        int previousTile = -1;
        for (int[] row : gameState) {
            for (int tile : row) {
                if (tile != previousTile + 1) {
                    return false;
                }
                previousTile = tile;
            }
        }
        return true;
    }

    private int[] findEmptyTilePosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameState[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalStateException("Empty tile not found in the game state");
    }

    private int[] getNewPosition(int[] currentPosition, int direction) {
        int[] newPosition = Arrays.copyOf(currentPosition, currentPosition.length);
        switch (direction) {
            case 0:
                newPosition[0]--;
                break;
            case 1:
                newPosition[0]++;
                break;
            case 2:
                newPosition[1]--;
                break;
            case 3:
                newPosition[1]++;
                break;
        }
        return newPosition;
    }

    private boolean isValidPosition(int[] position) {
        int row = position[0];
        int col = position[1];
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private void swapTiles(int[] position1, int[] position2) {
        int temp = gameState[position1[0]][position1[1]];
        gameState[position1[0]][position1[1]] = gameState[position2[0]][position2[1]];
        gameState[position2[0]][position2[1]] = temp;
    }


    public int[] findTilePosition(int tileValue) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameState[i][j] == tileValue) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public boolean isAdjacent(int[] tilePosition, int[] emptyTilePosition) {
        int rowDiff = Math.abs(tilePosition[0] - emptyTilePosition[0]);
        int colDiff = Math.abs(tilePosition[1] - emptyTilePosition[1]);
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }


}
