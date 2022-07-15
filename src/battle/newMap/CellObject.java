package battle.newMap;

import java.awt.*;

public class CellObject {
    private Point tilePos;
    private BuffCellType buffCellType;
    private ObjectInCell objectInCell;

    public CellObject(Point tilePos, BuffCellType buffCellType, ObjectInCell objectInCell) {
        this.tilePos = tilePos;
        this.buffCellType = buffCellType;
        this.objectInCell = objectInCell;
    }

    public void buildTower(int towerId, int level) {
        this.objectInCell = new Tower(towerId, level, tilePos);
    }

    public Point getTilePos() {
        return tilePos;
    }

    public void setTilePos(Point tilePos) {
        this.tilePos = tilePos;
    }

    public BuffCellType getBuffCellType() {
        return buffCellType;
    }

    public void setBuffCellType(BuffCellType buffCellType) {
        this.buffCellType = buffCellType;
    }

    public ObjectInCell getObjectInCell() {
        return objectInCell;
    }

    public void setObjectInCell(ObjectInCell objectInCell) {
        this.objectInCell = objectInCell;
    }

    @Override
    public String toString() {
        return "CellObject{" +
                "tilePos=" + tilePos.x + "," + tilePos.y +
                ", buffCellType=" + buffCellType +
                ", objectInCell=" + objectInCell +
                '}';
    }
}
