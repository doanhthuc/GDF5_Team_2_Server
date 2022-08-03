package battle.newMap;

public class ObjectInTile {
    private long entityId;
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

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
