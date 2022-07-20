package battle.Common;

public enum EntityMode {
    PLAYER(0),
    OPPONENT(1);

    private int value;

    EntityMode(int value) {
        this.value = value;
    }
}
