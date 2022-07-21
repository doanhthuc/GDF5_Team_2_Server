package battle.component.effect;

import battle.config.GameConfig;

public class SlowEffect extends EffectComponent {
    private String name = "SlowEffect";
    private double duration;
    private double percent;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public SlowEffect(double duration, double percent) {
        super(GameConfig.COMPONENT_ID.SLOW_EFFECT);
        this.duration = duration;
        this.percent = percent;
    }

    public SlowEffect clone() {
        return new SlowEffect(this.duration, this.percent);
    }

    public void reset(double duration, double percent) {
        this.duration = duration;
        this.percent = percent;
    }
}
