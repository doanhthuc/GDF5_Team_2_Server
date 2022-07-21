package battle.newMap;

public enum ObjectInTileType {
    NONE(0),
    TREE(1),
    TOWER(2),
    PIT(3);

    public final int value;

    private static final ObjectInTileType[] valueList = values();

    ObjectInTileType(final int value) {
        this.value = value;
    }

    public static ObjectInTileType getObjectInCellTypeByTypeId(int typeId) {
        return valueList[typeId];
    }
}
