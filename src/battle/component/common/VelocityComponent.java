package battle.component.common;

import battle.common.Point;
import battle.config.GameConfig;

public class VelocityComponent extends Component {
    private String name = "VelocityComponent";
    public static int typeID = GameConfig.COMPONENT_ID.VELOCITY;
    private double speedX;
    private double speedY;
    private double originSpeedX;
    private double originSpeedY;
    private PositionComponent dynamicPosition;
    private double originSpeed;

    public VelocityComponent(double speedX, double speedY, PositionComponent dynamicPosition) {
        super(GameConfig.COMPONENT_ID.VELOCITY);
        this.reset(speedX,speedY,dynamicPosition);
    }

    public void reset(double speedX, double speedY, PositionComponent dynamicPosition) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.dynamicPosition = dynamicPosition;
        this.originSpeed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        this.originSpeedX = this.speedX;
        this.originSpeedY = this.speedY;
    }

    public double calculateSpeed(double speedX, double speedY) {
        return Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }

    public double getSpeedX() {
        return this.speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return this.speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getOriginSpeedX() {
        return originSpeedX;
    }

    public void setOriginSpeedX(double originSpeedX) {
        this.originSpeedX = originSpeedX;
    }

    public double getOriginSpeedY() {
        return originSpeedY;
    }

    public void setOriginSpeedY(double originSpeedY) {
        this.originSpeedY = originSpeedY;
    }

    public PositionComponent getDynamicPosition() {
        return dynamicPosition;
    }

    public void setDynamicPosition(PositionComponent dynamicPosition) {
        this.dynamicPosition = dynamicPosition;
    }

    public double getOriginSpeed() {
        return originSpeed;
    }

    public void setOriginSpeed(double originSpeed) {
        this.originSpeed = originSpeed;
    }
}