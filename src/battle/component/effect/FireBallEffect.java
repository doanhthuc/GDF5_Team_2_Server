package battle.component.effect;

import battle.common.Point;
import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class FireBallEffect extends Component {
    private String name = "FireBallEffect";
    public static int typeID = GameConfig.COMPONENT_ID.ACCELERATION;
    private double a;
    private double accTime;
    private double maxDuration;
    private Point startPos;
    private Point endPos;
    private double v0;
    private double x;
    private double y;

    public FireBallEffect(double a, double maxDuration, Point startPos, Point endPos, double v0) {
        super(GameConfig.COMPONENT_ID.ACCELERATION);
        this.reset(a, maxDuration, startPos, endPos, v0);
    }

    public void reset(double a, double maxDuration, Point startPos, Point endPos, double v0) {
        this.a = a;
        this.accTime = 0;
        this.maxDuration = maxDuration;
        this.startPos = startPos;
        this.endPos = endPos;
        this.v0 = v0;
    }

    public FireBallEffect clone(ComponentFactory componentFactory) throws Exception {
        return componentFactory
                .createFireBallEffect(this.a, this.maxDuration, this.startPos, this.endPos, this.v0);
    }

    public void add(Point otherAcceleration) {
        this.x += otherAcceleration.x;
        this.y += otherAcceleration.y;
    }

    public double calculateSpeed(double speedX, double speedY) {
        return Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getAccTime() {
        return accTime;
    }

    public void setAccTime(double accTime) {
        this.accTime = accTime;
    }

    public double getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(double maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Point getStartPos() {
        return startPos;
    }

    public void setStartPos(Point startPos) {
        this.startPos = startPos;
    }

    public Point getEndPos() {
        return endPos;
    }

    public void setEndPos(Point endPos) {
        this.endPos = endPos;
    }

    public double getV0() {
        return v0;
    }

    public void setV0(double v0) {
        this.v0 = v0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
