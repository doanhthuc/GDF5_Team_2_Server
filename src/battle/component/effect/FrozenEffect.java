package battle.component.effect;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

public class FrozenEffect extends EffectComponent {
    private String name ="Frozen Effect";
    public static int typeID = GameConfig.COMPONENT_ID.FROZEN_EFFECT;
    private double duration;
    private double countdown;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    public FrozenEffect(double duration)
    {
        super(GameConfig.COMPONENT_ID.FROZEN_EFFECT);
        this.duration=duration;
        this.countdown=this.duration;
    }
    public FrozenEffect clone()
    {
        try {
            return ComponentFactory.getInstance().createFrozenEffect(this.duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(double duration)
    {
        this.duration=duration;
        this.countdown=this.duration;
    }
}
