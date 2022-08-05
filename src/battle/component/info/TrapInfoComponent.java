package battle.component.info;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class TrapInfoComponent extends Component {
    private String name = "TrapInfo";
    public static int typeID = GameConfig.COMPONENT_ID.TRAP_INFO;
    private double delayTrigger;
    private boolean isTriggered;

    public TrapInfoComponent(double delayTrigger) {
        super(GameConfig.COMPONENT_ID.TRAP_INFO);
        this.reset(delayTrigger);
    }

    public void reset(double delayTrigger) {
        this.delayTrigger = delayTrigger;
        this.isTriggered = false;
    }

    public void setTriggered(boolean triggered) {
        isTriggered = triggered;
    }

    public boolean isTriggered() {
        return this.isTriggered;
    }

    public double getDelayTrigger() {
        return delayTrigger;
    }

    public void setDelayTrigger(double delayTrigger) {
        this.delayTrigger = delayTrigger;
    }

//    public TrapInfoComponent clone() {
//        try {
//            return ComponentFactory.getInstance().createTrapInfoComponent(this.delayTrigger);
//        } catch (Exception e) {
//            return null;
//        }
//    }
}