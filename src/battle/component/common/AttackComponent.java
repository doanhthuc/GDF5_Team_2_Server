package battle.component.common;

import battle.component.effect.DamageEffect;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;

import java.util.ArrayList;
import java.util.List;

public class AttackComponent extends Component {
    private String name = "AttackComponent";
    private int originDamage;
    private double damage;
    private int targetStrategy;
    private double range;
    private double originSpeed;
    private double speed;
    private double countdown;
    private List<EffectComponent> effects = new ArrayList<>();

    public AttackComponent(int damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects) {
        super(GameConfig.COMPONENT_ID.ATTACK);
        this.reset(damage, targetStrategy, range, speed, countdown, effects);
    }

    public void reset(int damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects) {
        this.originDamage = damage;
        this.damage = damage;
        this.targetStrategy = targetStrategy;
        this.range = range;
        this.speed = speed;
        this.countdown = countdown;
        this.effects.add(new DamageEffect(this.damage));
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
        for (int i = 0; i < this.effects.size(); i++) {
            DamageEffect effect = (DamageEffect) this.effects.get(i);
            if (effect.getTypeID() == GameConfig.COMPONENT_ID.DAMAGE_EFFECT) {
                effect.setDamage(this.damage);
            }
        }
    }

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


    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    public List<EffectComponent> getEffects() {
        return effects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}