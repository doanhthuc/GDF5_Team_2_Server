package battle.component.common;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class HealingAbilityComponent extends Component{
    private String name = "HealingAbilityComponent";
    public static int typeID = GameConfig.COMPONENT_ID.HEALING_ABILITY;
    private double range;
    private double healingRate;

    public HealingAbilityComponent(double range, double healingRate) {
        super(GameConfig.COMPONENT_ID.HEALING_ABILITY);
        this.reset(range, healingRate);
    }

    public void reset(double range, double healingRate) {
        this.range = range;
        this.healingRate = healingRate;
    }

    public HealingAbilityComponent clone() {
        try {
            return ComponentFactory.getInstance().createHealingAbilityComponent(this.range, this.healingRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
