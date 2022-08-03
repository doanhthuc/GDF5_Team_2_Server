package battle.newMap;

import java.awt.*;

public class TileObject {
    private Point tilePos;
    private TileType tileType;
    private ObjectInTile objectInTile;

    public TileObject(Point tilePos, TileType tileType, ObjectInTile objectInTile) {
        this.tilePos = tilePos;
        this.tileType = tileType;
        this.objectInTile = objectInTile;
    }

    public Tower buildTower(int towerId, int level) {
        if (this.objectInTile.getObjectInCellType() == ObjectInTileType.NONE) {
            this.objectInTile = new Tower(towerId, level, this.tilePos);
        } else {
            throw new IllegalStateException("CellObject already has an objectInCell");
        }
        return (Tower) this.objectInTile;
    }

    public ObjectInTile destroyTower() {
        if (this.objectInTile.getObjectInCellType() == ObjectInTileType.TOWER) {
            this.objectInTile = new ObjectInTile(ObjectInTileType.NONE);
        } else {
            throw new IllegalStateException("TileObject does not have a tower");
        }
        return this.objectInTile;
    }

    public Point getTilePos() {
        return tilePos;
    }

    public void setTilePos(Point tilePos) {
        this.tilePos = tilePos;
    }

    public TileType getBuffCellType() {
        return tileType;
    }

    public void setBuffCellType(TileType tileType) {
        this.tileType = tileType;
    }

    public ObjectInTile getObjectInCell() {
        return objectInTile;
    }

    public void setObjectInCell(ObjectInTile objectInTile) {
        this.objectInTile = objectInTile;
    }

    @Override
    public String toString() {
        return "CellObject{" +
                "tilePos=" + tilePos.x + "," + tilePos.y +
                ", buffCellType=" + tileType +
                ", objectInCell=" + objectInTile +
                '}';
    }
}
