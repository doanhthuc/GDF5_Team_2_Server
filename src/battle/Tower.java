package battle;


import java.awt.*;

public class Tower {
    private int id;
    private int level;
    private Point tilePos;

    public Tower(int id, int level, Point tilePos) {
        this.id = id;
        this.level = level;
        this.tilePos = tilePos;
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
}
