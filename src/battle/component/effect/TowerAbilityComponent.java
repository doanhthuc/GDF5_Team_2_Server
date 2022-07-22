package battle.component.effect;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class TowerAbilityComponent extends Component {
    public static String name = "TowerAbilityComponent";
    public static int typeId = GameConfig.COMPONENT_ID.TOWER_ABILITY;
    private double range;
    private EffectComponent effect;

    public TowerAbilityComponent(double range, EffectComponent effect) {
        super(typeId);
        this.range = range;
        this.effect = effect;
    }

    public TowerAbilityComponent clone ()  {
        try {
            return ComponentFactory.getInstance().createTowerAbilityComponent(this.range, this.effect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset (double range, EffectComponent effect) {
        this.range = range;
        this.effect = effect;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public EffectComponent getEffect() {
        return effect;
    }

    public void setEffect(EffectComponent effect) {
        this.effect = effect;
    }
}
