package battle.component.towerskill;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class GoatSlowEffectComponent extends Component {
    private String name = "GoatSlowEffectComponent";
    public static int typeID = GameConfig.COMPONENT_ID.GOAT_SLOW_EFFECT;
    private double percent;

    public GoatSlowEffectComponent(double percent) {
        super(GameConfig.COMPONENT_ID.GOAT_SLOW_EFFECT);
        this.percent = percent;
    }

    public GoatSlowEffectComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createGoatSlowEffectComponent(this.percent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void reset(double percents) {
        this.percent = percent;
    }
}
