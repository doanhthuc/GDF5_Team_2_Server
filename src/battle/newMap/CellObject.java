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

    public Tower buildTower(int towerId, int level) {
        if (this.objectInCell.getObjectInCellType() == ObjectInCellType.NONE) {
            this.objectInCell = new Tower(towerId, level, this.tilePos);
        } else {
            throw new IllegalStateException("CellObject already has an objectInCell");
        }
        return (Tower) this.objectInCell;
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
