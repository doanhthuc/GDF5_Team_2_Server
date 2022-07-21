package battle.component.common;

import battle.common.Point;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class PositionComponent extends Component {
    private String name = "PositionComponent";
    private int x;
    private int y;
    private int moveDistance;

    public PositionComponent(int x, int y) {
        super(GameConfig.COMPONENT_ID.POSITION);
        this.x = x;
        this.y = y;
    }

    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        this.moveDistance = 0;
    }

    public PositionComponent clone(){
        try {
            return ComponentFactory.getInstance().createPositionComponent(this.x,this.y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getX() {
        return x;
    }

    public PositionComponent setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public PositionComponent setY(int y) {
        this.y = y;
        return this;
    }

    public Point getPos() {
        return new Point(x, y);
    }

    public String toString() {
        return (this.x + " " + this.y);
    }

    public int getMoveDistance() {
        return moveDistance;
    }

    public void setMoveDistance(int moveDistance) {
        this.moveDistance = moveDistance;
    }
}