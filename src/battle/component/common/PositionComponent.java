package battle.component.common;

import battle.common.Point;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class PositionComponent extends Component {
    private String name = "PositionComponent";
    public static int typeID = GameConfig.COMPONENT_ID.POSITION;
    private double x;
    private double y;
    private double moveDistance;



    public PositionComponent(double x, double y) {
        super(GameConfig.COMPONENT_ID.POSITION);
        this.reset(x,y);
    }

    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.moveDistance=0;
    }

    public PositionComponent clone(){
        try {
            return ComponentFactory.getInstance().createPositionComponent(this.x,this.y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public double getX() {
        return x;
    }

    public PositionComponent setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public PositionComponent setY(double y) {
        this.y = y;
        return this;
    }

    public Point getPos() {
        return new Point(x, y);
    }

    public String toString() {
        return (this.x + " " + this.y);
    }
    public double getMoveDistance() {
        return moveDistance;
    }

    public void setMoveDistance(double moveDistance) {
        this.moveDistance = moveDistance;
    }
}