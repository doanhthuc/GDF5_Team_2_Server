package battle.newMap;

public class ObjectInTile {
    private ObjectInTileType objectInTileType;

    public ObjectInTile(ObjectInTileType objectInTileType) {
        this.objectInTileType = objectInTileType;
    }

    public ObjectInTileType getObjectInCellType() {
        return objectInTileType;
    }

    public void setObjectInCellType(ObjectInTileType objectInTileType) {
        this.objectInTileType = objectInTileType;
    }
}
