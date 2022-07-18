package battle.component.Component;

import battle.common.Point;
import battle.config.GameConfig;

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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point getPos() {
        return new Point(x, y);
    }

    public String toString() {
        return (this.x + " " + this.y);
    }
}