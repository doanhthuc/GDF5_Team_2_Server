package battle.Component.EffectComponent;

import battle.Config.GameConfig;

public class SlowEffect extends EffectComponent {
    public String name = "SlowEffect";
    public double duration;
    public double percent;

    public SlowEffect(double duration, double percent) {
        super(GameConfig.COMPONENT_ID.SLOW_EFFECT);
        this.duration = duration;
        this.percent = percent;
    }

    public SlowEffect clone() {
        return new SlowEffect(this.duration, this.percent);
    }

    public void reset() {

    }
}
