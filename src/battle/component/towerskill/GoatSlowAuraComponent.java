package battle.component.towerskill;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class GoatSlowAuraComponent extends Component {
    private String name = "GoatSlowAuraComponent";
    public static int typeID = GameConfig.COMPONENT_ID.GOAT_SLOW_AURA;
    private double range;
    private double percent;

    public GoatSlowAuraComponent(double percent, double range) {
        super(GameConfig.COMPONENT_ID.GOAT_SLOW_AURA);
        this.range = range;
        this.percent = percent;
    }

    public GoatSlowAuraComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createGoatSlowAuraComponent(this.percent, this.range);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void reset(double percent, double range) {
        this.range = range;
        this.percent = percent;
    }
}
