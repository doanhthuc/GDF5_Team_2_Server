package battle.common;

import java.util.ArrayList;
import java.util.List;


public class QuadTree {
    private int MAX_OBJECTS;
    private int MAX_LEVELS;
    private int currentLevel;
    private Rect boundingBox;
    private List<QuadTreeData> listObjects;
    private QuadTree[] nodes;

    public QuadTree(int level, Rect rect) {
        this.MAX_OBJECTS = 10;
        this.MAX_LEVELS = 15;

        this.currentLevel = level;
        this.boundingBox = rect;
        this.listObjects = new ArrayList<>();

    }

    public void clear() {
        this.listObjects.clear();
        if (this.nodes != null) {
            for (QuadTree node : this.nodes) {
                if (node != null) {
                    node.clear();
                }
            }
        }
    }

    private void split() {
        this.nodes = new QuadTree[4];
        double halfW = Math.floor(this.boundingBox.width / 2);
        double halfH = Math.floor(this.boundingBox.height / 2);
        double x = this.boundingBox.x;
        double y = this.boundingBox.y;

        this.nodes[QuadTreeDirection.BOTTOM_LEFT.value] = new QuadTree(this.currentLevel + 1, new Rect(x, y, halfW, halfH));
        this.nodes[QuadTreeDirection.BOTTOM_RIGHT.value] = new QuadTree(this.currentLevel + 1, new Rect(x + halfW, y, halfW, halfH));
        this.nodes[QuadTreeDirection.TOP_LEFT.value] = new QuadTree(this.currentLevel + 1, new Rect(x, y + halfH, halfW, halfH));
        this.nodes[QuadTreeDirection.TOP_RIGHT.value] = new QuadTree(this.currentLevel + 1, new Rect(x + halfW, y + halfH, halfW, halfH));

    }

    private int getIndex (Rect pRect) {
        int index = -1;
        double verticalMidpoint = this.boundingBox.x + (this.boundingBox.width / 2);
        double horizontalMidpoint = this.boundingBox.y + (this.boundingBox.height / 2);
        boolean isTop = pRect.y > horizontalMidpoint;
        boolean isBottom = pRect.y < horizontalMidpoint && pRect.y + pRect.height < horizontalMidpoint;
        boolean isLeft = pRect.x < verticalMidpoint && pRect.x + pRect.width < verticalMidpoint;
        boolean isRight = pRect.x > verticalMidpoint;

        if (isLeft) {
            if (isTop) {
                index = QuadTreeDirection.TOP_LEFT.value;
            } else if (isBottom) {
                index = QuadTreeDirection.BOTTOM_LEFT.value;
            }
        } else if (isRight) {
            if (isTop) {
                index = QuadTreeDirection.TOP_RIGHT.value;
            } else if (isBottom) {
                index = QuadTreeDirection.BOTTOM_RIGHT.value;
            }
        }
        return index;
    }

    public void insert (QuadTreeData quadTreeData) {
        if (this.nodes != null) {
            int index = this.getIndex(quadTreeData.pRect);
            if (index != -1) {
                this.nodes[index].insert(quadTreeData);
                return;
            }
        }
        this.listObjects.add(quadTreeData);
        if (this.listObjects.size() > this.MAX_OBJECTS && this.currentLevel < this.MAX_LEVELS) {
            if (this.nodes == null) {
                this.split();
            }

            for (int i = 0; i < this.listObjects.size(); i++) {
                int index = this.getIndex(this.listObjects.get(i).pRect);
                if (index != -1) {
                    this.nodes[index].insert(this.listObjects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    public List<QuadTreeData> retrieve (Rect pRect) {
        List<QuadTreeData> returnObjects = new ArrayList<>();
        int index = this.getIndex(pRect);
        if (this.nodes != null && index != -1) {
            List<QuadTreeData> res = this.nodes[index].retrieve(pRect);
            if (res.size() > 0) {
                returnObjects.addAll(res);
            }
        }
        returnObjects.addAll(this.listObjects);
        return returnObjects;
    }

}
