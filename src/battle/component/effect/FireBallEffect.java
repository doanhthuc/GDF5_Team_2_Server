package battle.component.effect;

import battle.common.Point;
import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class FireBallEffect extends Component {
    private String name = "FireBallEffect";
    public static int typeID = GameConfig.COMPONENT_ID.ACCELERATION;
    private double acceleration;
    private double accTime;
    private double maxDuration;
    private Point startPos;
    private Point endPos;
    private double velocityStart;

    public FireBallEffect(double a, double maxDuration, Point startPos, Point endPos, double v0) {
        super(GameConfig.COMPONENT_ID.ACCELERATION);
        this.reset(a, maxDuration, startPos, endPos, v0);
    }

    public void reset(double a, double maxDuration, Point startPos, Point endPos, double v0) {
        this.acceleration = a;
        this.accTime = 0;
        this.maxDuration = maxDuration;
        this.startPos = startPos;
        this.endPos = endPos;
        this.velocityStart = v0;
    }

    public FireBallEffect clone(ComponentFactory componentFactory) throws Exception {
        return componentFactory
                .createFireBallEffect(this.acceleration, this.maxDuration, this.startPos, this.endPos, this.velocityStart);
    }


    public double calculateSpeed(double speedX, double speedY) {
        return Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
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

    public double getVelocityStart() {
        return velocityStart;
    }

    public void setVelocityStart(double velocityStart) {
        this.velocityStart = velocityStart;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(acceleration);
        bf.putDouble(accTime);
        bf.putDouble(maxDuration);
        bf.putDouble(startPos.x);
        bf.putDouble(startPos.y);
        bf.putDouble(endPos.x);
        bf.putDouble(endPos.y);
        bf.putDouble(velocityStart);
    }
}
