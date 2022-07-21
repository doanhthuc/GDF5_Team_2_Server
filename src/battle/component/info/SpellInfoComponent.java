package battle.component.info;

import battle.component.common.Component;
import battle.component.common.PositionComponent;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;
import battle.system.SpellSystem;

import java.util.List;

public class SpellInfoComponent extends Component {
    private String name = "SpellInfoComponent";
    public static int typeID = GameConfig.COMPONENT_ID.SPELL;
    private PositionComponent position;
    private List<EffectComponent> effects;
    private double range;
    private double countdown;

    public SpellInfoComponent(PositionComponent position, List<EffectComponent> effects, double range, double duration) {
        super(GameConfig.COMPONENT_ID.SPELL);
        this.reset(position, effects, range, duration);
    }

    public void reset(PositionComponent position, List<EffectComponent> effects, double range, double duration) {
        this.position = position;
        this.effects = effects;
        this.range = range;
        this.countdown = duration;
    }

    public PositionComponent getPosition() {
        return position;
    }

    public void setPositon(PositionComponent positon) {
        this.position = positon;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    public SpellInfoComponent clone(){
        try {
            return ComponentFactory.getInstance().createSpellInfoComponent(this.position,this.effects,this.range,this.countdown);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EffectComponent> getEffects() {
        return effects;
    }

    public void setEffects(List<EffectComponent> effects) {
        this.effects = effects;
    }
}
