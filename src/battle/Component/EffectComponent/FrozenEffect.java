package battle.Component.EffectComponent;

import battle.Config.GameConfig;

public class FrozenEffect extends EffectComponent {
    private String name ="Frozen Effect";
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
        return new FrozenEffect(duration);
    }
}
