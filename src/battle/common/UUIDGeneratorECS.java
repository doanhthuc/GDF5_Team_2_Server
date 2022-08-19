package battle.common;

public class UUIDGeneratorECS {
    private final long playerStartEntityID = 0;
    private final long opponentStartEntityID = 100000;
    private long playerEntityId;
    private long opponentEntityId;
    private long componentID = 0;
    private long systemID = 0;

    public UUIDGeneratorECS() {
        this.playerEntityId = playerStartEntityID;
        this.opponentEntityId = opponentStartEntityID;
    }

    public long genEntityID(EntityMode entityMode) {
        if (entityMode == EntityMode.PLAYER) {
            return ++playerEntityId;
        } else {
            return ++opponentEntityId;
        }
    }

    public long genComponentID() {
        return ++componentID;
    }

    public long getPlayerStartEntityID() {
        return playerStartEntityID;
    }

    public long getOpponentStartEntityID() {
        return opponentStartEntityID;
    }

    public long genSystemID() {
        return ++systemID;
    }
}
