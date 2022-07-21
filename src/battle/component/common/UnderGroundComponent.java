package battle.component.common;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class UnderGroundComponent extends Component {
    private String name = "UnderGroundComponent";
    public static int typeID = GameConfig.COMPONENT_ID.UNDER_GROUND;
    private int currentPathIdx;
    private int trigger;
    private boolean isInGround;
    private double disableMoveDistance;
    public UnderGroundComponent() {
        super(GameConfig.COMPONENT_ID.UNDER_GROUND);
        this.currentPathIdx = 0;
        this.trigger = 0;
        this.isInGround = false;
        this.disableMoveDistance = 0;
    }
    public void reset(){
        this.currentPathIdx = 0;
        this.trigger = 0;
        this.isInGround = false;
        this.disableMoveDistance = 0;
    }
    public UnderGroundComponent clone()
    {
        try {
            return ComponentFactory.getInstance().createUnderGroundComponent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCurrentPathIdx() {
        return currentPathIdx;
    }

    public void setCurrentPathIdx(int currentPathIdx) {
        this.currentPathIdx = currentPathIdx;
    }

    public int getTrigger() {
        return trigger;
    }

    public void setTrigger(int trigger) {
        this.trigger = trigger;
    }

    public boolean isInGround() {
        return isInGround;
    }

    public void setInGround(boolean inGround) {
        isInGround = inGround;
    }

    public double getDisableMoveDistance() {
        return disableMoveDistance;
    }

    public void setDisableMoveDistance(double disableMoveDistance) {
        this.disableMoveDistance = disableMoveDistance;
    }
}
