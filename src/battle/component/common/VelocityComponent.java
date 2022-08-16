package battle.component.common;

import battle.Battle;
import battle.common.Point;
import battle.config.GameConfig;
import battle.entity.EntityECS;

public class VelocityComponent extends Component {
    private String name = "VelocityComponent";
    public static int typeID = GameConfig.COMPONENT_ID.VELOCITY;
    private double speedX;
    private double speedY;
    private double originSpeedX;
    private double originSpeedY;
    private double originSpeed;
    private Point staticPosition;
    private int dynamicEntityId = -1;

    public VelocityComponent(double speedX, double speedY, int dynamicEntityId) {
        super(GameConfig.COMPONENT_ID.VELOCITY);
        this.reset(speedX, speedY, dynamicEntityId);
    }

    public VelocityComponent(double speedX, double speedY, Point staticPosition) {
        super(GameConfig.COMPONENT_ID.VELOCITY);
        this.reset(speedX, speedY, staticPosition);
    }

    public VelocityComponent(double speedX, double speedY) {
        super(GameConfig.COMPONENT_ID.VELOCITY);
        this.reset(speedX, speedY);
    }

    public void reset(double speedX, double speedY, int dynamicEntityId) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.dynamicEntityId = dynamicEntityId;
        this.originSpeed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        this.originSpeedX = this.speedX;
        this.originSpeedY = this.speedY;
    }

    public void reset(double speedX, double speedY, Point staticPosition) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.staticPosition = staticPosition;
        this.originSpeed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        this.originSpeedX = this.speedX;
        this.originSpeedY = this.speedY;
    }

    public void reset(double speedX, double speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.originSpeed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        this.originSpeedX = this.speedX;
        this.originSpeedY = this.speedY;
    }

    public double calculateSpeed(double speedX, double speedY) {
        return Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }

    public PositionComponent getDynamicPosition(Battle battle) {
        if (this.dynamicEntityId == -1) return null;

        EntityECS entityECS = battle.getEntityManager().getEntity(this.dynamicEntityId);

        if ((entityECS != null && entityECS.getActive())) {
            return (PositionComponent) entityECS.getComponent(PositionComponent.typeID);
        }

        return null;
    }

    public int getDynamicEntityId() {
        return this.dynamicEntityId;
    }

    public boolean hasDynamicEntityId() {
        return this.dynamicEntityId != -1;
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

    public double getOriginSpeed() {
        return originSpeed;
    }

    public void setOriginSpeed(double originSpeed) {
        this.originSpeed = originSpeed;
    }

    public Point getStaticPosition() {
        return staticPosition;
    }
}