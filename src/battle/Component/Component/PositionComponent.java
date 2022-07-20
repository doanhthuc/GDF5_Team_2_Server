package battle.Component.Component;

import battle.Common.Point;
import battle.Config.GameConfig;

public class PositionComponent extends Component {
    private String name = "PositionComponent";
    private int x;
    private int y;

    public PositionComponent(int x, int y) {
        super(GameConfig.COMPONENT_ID.POSITION);
        this.x = x;
        this.y = y;
    }

    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
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
}