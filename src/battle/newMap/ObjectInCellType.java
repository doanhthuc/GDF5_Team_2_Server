package battle.newMap;

public enum ObjectInCellType {
    NONE(0),
    TREE(1),
    TOWER(2),
    PIT(3);

    public final int value;

    private static final ObjectInCellType[] valueList = values();

    ObjectInCellType(final int value) {
        this.value = value;
    }

    public static ObjectInCellType getObjectInCellType(int typeId) {
        switch (typeId) {
            case 1:
                return ObjectInCellType.TREE;
            case 2:
                return ObjectInCellType.TOWER;
            case 3:
                return ObjectInCellType.PIT;
            default:
                return ObjectInCellType.NONE;
        }
    }

    public static ObjectInCellType getObjectInCellTypeByTypeId(int typeId) {
        return valueList[typeId];
    }
}
