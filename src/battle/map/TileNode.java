package battle.map;

import java.awt.*;

public class TileNode {
    public TileNode father;
    public int h;
    public int g;
    public Point pos;

    public TileNode(int h, int g, Point p) {
        this.h = h;
        this.g = g;
        this.pos = p;
    }
}
