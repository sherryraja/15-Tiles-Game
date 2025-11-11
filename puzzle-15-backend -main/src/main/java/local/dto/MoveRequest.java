package local.dto;

public class MoveRequest {
    private int tileValue;

    public MoveRequest() {}
    public MoveRequest(int tileValue) { this.tileValue = tileValue; }

    public int getTileValue() { return tileValue; }
    public void setTileValue(int tileValue) { this.tileValue = tileValue; }
}
