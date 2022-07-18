package battle.component.Component;

import battle.component.EffectComponent.DamageEffect;
import battle.component.EffectComponent.EffectComponent;
import battle.config.GameConfig;

import java.util.ArrayList;

public class AttackComponent extends Component {
    private String name="AttackComponent";
    private int originDamage;
    private double _damage;
    private int targetStrategy;
    private double range;
    private double originSpeed;
    private double speed;
    private double countdown;
    private ArrayList<EffectComponent> effects = new ArrayList<>();

    public int getOriginDamage() {
        return originDamage;
    }

    public void setOriginDamage(int originDamage) {
        this.originDamage = originDamage;
    }

    public int getTargetStrategy() {
        return targetStrategy;
    }

    public void setTargetStrategy(int targetStrategy) {
        this.targetStrategy = targetStrategy;
    }

    public double getRange() {
        return range;
    }


    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    public ArrayList<EffectComponent> getEffects() {
        return effects;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttackComponent(int damage, int targetStrategy, double range, double speed, double countdown, EffectComponent effects) {
        super(GameConfig.COMPONENT_ID.ATTACK);
        this.originDamage = damage;
        this._damage = damage;
        this.targetStrategy = targetStrategy;
        this.range = range;
        this.speed = speed;
        this.countdown = countdown;
        this.effects.add(new DamageEffect(this._damage));
    }

    public void reset(int damage, int targetStrategy, double range, double speed, double countdown, EffectComponent effects) {
        this.originDamage = damage;
        this._damage = damage;
        this.targetStrategy = targetStrategy;
        this.range = range;
        this.speed = speed;
        this.countdown = countdown;
        this.effects.add(new DamageEffect(this._damage));
    }

    public void setDamage(int damage) {
        this._damage = damage;
        for (int i = 0; i < this.effects.size(); i++) {
            DamageEffect effect = (DamageEffect) this.effects.get(i);
            if (effect.typeID == GameConfig.COMPONENT_ID.DAMAGE_EFFECT) {
                effect.damage = this._damage;
            }
        }
    }

    public double getDamage() {
        return this._damage;
    }
}