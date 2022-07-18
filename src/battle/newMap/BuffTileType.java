package battle.newMap;

public enum BuffTileType {
    NONE(0),
    ATTACK_SPEED_UP(1),
    DAMAGE_UP(3),
    ATTACK_RANGE_UP(2);

    public final int value;

    BuffTileType(final int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BuffCellType{" +
                "value=" + value +
                '}';
    }
}
