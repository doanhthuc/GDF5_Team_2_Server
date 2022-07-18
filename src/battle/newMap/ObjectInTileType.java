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

    public static ObjectInTileType getObjectInCellType(int typeId) {
        switch (typeId) {
            case 1:
                return ObjectInTileType.TREE;
            case 2:
                return ObjectInTileType.TOWER;
            case 3:
                return ObjectInTileType.PIT;
            default:
                return ObjectInTileType.NONE;
        }
    }

    public static ObjectInTileType getObjectInCellTypeByTypeId(int typeId) {
        return valueList[typeId];
    }
}
