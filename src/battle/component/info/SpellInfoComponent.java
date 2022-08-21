package battle.component.info;

import battle.common.Point;
import battle.component.common.Component;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.util.List;

public class SpellInfoComponent extends Component {
    private String name = "SpellInfoComponent";
    public static int typeID = GameConfig.COMPONENT_ID.SPELL;
    private static final double DELAY_DESTROY_TIME = 1;
    private Point position;
    private List<EffectComponent> effects;
    private double range;
    private double delay;
    private double delayDestroy;
    private boolean isTriggered;

    public SpellInfoComponent(Point position, List<EffectComponent> effects, double range, double duration) {
        this(position, effects, range, duration, duration + DELAY_DESTROY_TIME, false);
    }

    public SpellInfoComponent(Point position, List<EffectComponent> effects, double range, double duration, double delayDestroy, boolean isTriggered) {
        super(GameConfig.COMPONENT_ID.SPELL);
        this.reset(position, effects, range, duration, delayDestroy, isTriggered);
    }

    public void reset(Point position, List<EffectComponent> effects, double range, double duration) {
        this.reset(position, effects, range, duration, duration + DELAY_DESTROY_TIME, false);
    }

    public void reset(Point position, List<EffectComponent> effects, double range, double duration, double delayDestroy, boolean isTriggered) {
        this.position = position;
        this.effects = effects;
        this.range = range;
        this.delay = duration;
        this.delayDestroy = delayDestroy;
        this.isTriggered = isTriggered;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public SpellInfoComponent clone(ComponentFactory componentFactory) throws Exception {
        try {
            return componentFactory.createSpellInfoComponent(this.position, this.effects, this.range, this.delay, this.delayDestroy, this.isTriggered);
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

    public boolean isTriggered() {
        return isTriggered;
    }

    public void setTriggered(boolean triggered) {
        isTriggered = triggered;
    }

    public double getDelayDestroy() {
        return delayDestroy;
    }

    public void setDelayDestroy(double delayDestroy) {
        this.delayDestroy = delayDestroy;
    }
}

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(position.x);
        bf.putDouble(position.y);
        bf.putDouble(countdown);
        bf.putInt(effects.size());
        for (EffectComponent effectComponent : this.effects){
            effectComponent.createData(bf);
        }
    }