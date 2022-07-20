package battle.component.common;

import battle.common.Point;
import battle.config.GameConfig;

public class VelocityComponent extends Component {
    private String name = "VelocityComponent";
    private double speedX;
    private double speedY;
    private double originSpeedX;
    private double originSpeedY;
    private Point dynamicPosition;
    private double originSpeed;

    public VelocityComponent(double speedX, double speedY, Point dynamicPosition) {
        super(GameConfig.COMPONENT_ID.VELOCITY);
        this.reset(speedX,speedY,dynamicPosition);
    }

    public void reset(double speedX, double speedY, Point dynamicPosition) {
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
}