package battle.component.effect;

import battle.component.common.Component;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class TowerAbilityComponent extends Component {
    public static int typeID = GameConfig.COMPONENT_ID.TOWER_ABILITY;
    public static String name = "TowerAbilityComponent";
    private double range;
    private EffectComponent effect;

    public TowerAbilityComponent(double range, EffectComponent effect) {
        super(typeID);
        this.range = range;
        this.effect = effect;
    }

    public TowerAbilityComponent clone(ComponentFactory componentFactory) throws Exception {
        try {
            return componentFactory.createTowerAbilityComponent(this.range, this.effect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(double range, EffectComponent effect) {
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

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(range);
        effect.createData(bf);
    }
}
