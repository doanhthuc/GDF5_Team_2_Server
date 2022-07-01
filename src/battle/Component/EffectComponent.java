package battle.Component;

import battle.Config.GameConfig;

public class EffectComponent extends Component {
    public String name = "EffectComponent";

    public EffectComponent(int typeID) {
        this.typeID = typeID;
    }
}

class DamageEffect extends EffectComponent {
    public String name = "DamageEffect";
    double damage;

    public DamageEffect(double damage) {
        super(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        this.damage = damage;
    }

    public DamageEffect clone() {
        return new DamageEffect(this.damage);
    }

    public void reset() {

    }
}

class SlowEffect extends EffectComponent {
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
class FrozenEffect extends EffectComponent{
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

class BuffAttackSpeedEffect extends EffectComponent {
    public String name = "BuffAttackSpeedEffect";
    public double percent;

    public BuffAttackSpeedEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED);
        this.percent = percent;
    }

    public BuffAttackSpeedEffect clone() {
        return new BuffAttackSpeedEffect(this.percent);
    }

    public void reset() {

    }
}

class BuffAttackDamageEffect extends EffectComponent {
    public String name = "BuffAttackDamageEffect";
    public double percent;

    public BuffAttackDamageEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE);
        this.percent = percent;
    }

    public BuffAttackDamageEffect clone() {
        return new BuffAttackDamageEffect(this.percent);
    }

    public void reset() {

    };
}