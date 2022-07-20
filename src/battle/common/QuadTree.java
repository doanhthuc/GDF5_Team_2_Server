package battle.common;

import battle.entity.EntityECS;

import java.util.ArrayList;
import java.util.List;

class QuadTreeData {
    public Rect pRect;
    public EntityECS entity;

    public QuadTreeData(Rect pRect, EntityECS entity) {
        this.pRect = pRect;
        this.entity = entity;
    }
}

public class QuadTree {
    private int MAX_OBJECTS;
    private int MAX_LEVELS;
    private int currentLevel;
    private Rect boundingBox;
    private List<QuadTreeData> listObjects;
    private List<QuadTree> nodes;

    public QuadTree(int level, Rect rect) {
        this.MAX_OBJECTS = 10;
        this.MAX_LEVELS = 15;

        this.currentLevel = level;
        this.boundingBox = rect;
        this.listObjects = new ArrayList<>();

        this.nodes = new ArrayList<>();
    }

    private void split() {
        double halfW = Math.floor(this.boundingBox.width / 2);
        double halfH = Math.floor(this.boundingBox.height / 2);
        double x = this.boundingBox.x;
        double y = this.boundingBox.y;
        //FIXME
//        this.nodes[QuadTree.BOTTOM_LEFT] = new QuadTree(this.level + 1, cc.rect(x, y, halfW, halfH));
//        this.nodes[QuadTree.BOTTOM_RIGHT]  = new QuadTree(this.level + 1, cc.rect(x + halfW, y, halfW, halfH));
//        this.nodes[QuadTree.TOP_LEFT] = new QuadTree(this.level + 1, cc.rect(x, y + halfH, halfW, halfH));
//        this.nodes[QuadTree.TOP_RIGHT] = new QuadTree(this.level + 1, cc.rect(x + halfW, y + halfH, halfW, halfH));

    }
}
