package battle.Component.Component;

import battle.Common.Point;
import battle.Config.GameConfig;

public class VelocityComponent extends Component {
    public String name = "VelocityComponent";
    public double speedX;
    public double speedY;
    public double originSpeedX;
    public double originSpeedY;
    public Point dynamicPosition;
    public double originSpeed;

    public VelocityComponent(double speedX, double speedY, Point dynamicPosition) {
        super(GameConfig.COMPONENT_ID.VELOCITY);
        this.speedX = speedX;
        this.speedY = speedY;
        this.dynamicPosition = dynamicPosition;
        this.originSpeed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        this.originSpeedX = this.speedX;
        this.originSpeedY = this.speedY;
    }

    public void reset(double speedX, double speedY, Point dynamicPosition) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.dynamicPosition = dynamicPosition;
        this.originSpeed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        this.originSpeedX = this.speedX;
        this.originSpeedY = this.speedY;
    }

    public double calulateSpeed(double speedX, double speedY) {
        return Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }
}