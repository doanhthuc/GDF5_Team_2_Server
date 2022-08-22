package battle.component.effect;

import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.nio.ByteBuffer;

public class SlowEffect extends EffectComponent {
    private String name = "SlowEffect";
    public static int typeID = GameConfig.COMPONENT_ID.SLOW_EFFECT;
    private double duration;
    private double percent;
    private double countdown;

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
        this.reset(duration,percent);
    }

    public SlowEffect clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createSlowEffect(duration, percent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset(double duration, double percent) {
        this.duration = duration;
        this.percent = percent;
        this.countdown = this.duration;
    }

    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
        bf.putDouble(duration);
        bf.putDouble(percent);
        bf.putDouble(countdown);
    }
}
