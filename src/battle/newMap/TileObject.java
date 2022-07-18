package battle.newMap;

import java.awt.*;

public class TileObject {
    private Point tilePos;
    private BuffTileType buffTileType;
    private ObjectInTile objectInTile;

    public TileObject(Point tilePos, BuffTileType buffTileType, ObjectInTile objectInTile) {
        this.tilePos = tilePos;
        this.buffTileType = buffTileType;
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

    public Point getTilePos() {
        return tilePos;
    }

    public void setTilePos(Point tilePos) {
        this.tilePos = tilePos;
    }

    public BuffTileType getBuffCellType() {
        return buffTileType;
    }

    public void setBuffCellType(BuffTileType buffTileType) {
        this.buffTileType = buffTileType;
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
                ", buffCellType=" + buffTileType +
                ", objectInCell=" + objectInTile +
                '}';
    }
}
