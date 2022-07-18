package battle.newMap;


import java.awt.*;

public class Tower extends ObjectInTile {
    private int id;
    private int level;
    private Point tilePos;

    public Tower(int id, int level, Point tilePos) {
        super(ObjectInTileType.TOWER);
        this.id = id;
        this.level = level;
        this.tilePos = tilePos;
    }

    public Tower upgradeTower() {
        this.level++;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Point getTilePos() {
        return tilePos;
    }

    public void setTilePos(Point tilePos) {
        this.tilePos = tilePos;
    }

    @Override
    public String toString() {
        return "Tower{" +
                "id=" + id +
                ", level=" + level +
                ", tilePos=" + tilePos +
                '}' + '\n';
    }
}
