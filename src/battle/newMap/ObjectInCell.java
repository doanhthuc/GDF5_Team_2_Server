package battle.newMap;

public class ObjectInCell {
    private ObjectInCellType objectInCellType;

    public ObjectInCell(ObjectInCellType objectInCellType) {
        this.objectInCellType = objectInCellType;
    }

    public ObjectInCellType getObjectInCellType() {
        return objectInCellType;
    }

    public void setObjectInCellType(ObjectInCellType objectInCellType) {
        this.objectInCellType = objectInCellType;
    }
}
