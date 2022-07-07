package battle.Component.EffectComponent;

import battle.Config.GameConfig;

public class FrozenEffect extends EffectComponent {
    public String name ="Frozen Effect";
    public double duration;
    public double countdown;
    public FrozenEffect(double duration)
    {
        super(GameConfig.COMPONENT_ID.FROZEN_EFFECT);
        this.duration=duration;
        this.countdown=this.duration;
    }
}
