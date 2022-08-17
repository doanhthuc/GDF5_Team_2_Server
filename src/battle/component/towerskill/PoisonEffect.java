package battle.component.towerskill;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class PoisonEffect extends Component {
    private String name = "PoisonEffect";
    public static int typeID = GameConfig.COMPONENT_ID.POISON;
    private double duration;
    private double healthPerSecond;


    public PoisonEffect(double healthPerSecond, double duration) {
        super(GameConfig.COMPONENT_ID.POISON);
        this.reset(healthPerSecond, duration);
    }

    public PoisonEffect clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createPoisonEffect(healthPerSecond, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(double healthPerSecond, double duration) {
        this.healthPerSecond = healthPerSecond;
        this.duration = duration;

    }

    public void setHealthPerSecond(double healthPerSecond) {
        this.healthPerSecond = healthPerSecond;
    }

    public double getHealthPerSecond() {
        return healthPerSecond;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
