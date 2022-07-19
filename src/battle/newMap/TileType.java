package battle.newMap;

public enum TileType {
    NONE(0),
    ATTACK_SPEED_UP(1),
    ATTACK_RANGE_UP(2),
    DAMAGE_UP(3),
    PATH(4),
    TREE(5),
    PIT(6);

    public final int value;

    TileType(final int value) {
        this.value = value;
    }

    private static final TileType[] valueList = values();

    public static TileType getTileTypeByValue(int value) {
        return valueList[value];
    }

    @Override
    public String toString() {
        return "BuffCellType{" +
                "value=" + value +
                '}';
    }
}
