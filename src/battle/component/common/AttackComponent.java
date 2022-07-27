package battle.component.common;

import battle.component.effect.DamageEffect;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.util.ArrayList;
import java.util.List;

public class AttackComponent extends Component {
    public static int typeID = GameConfig.COMPONENT_ID.ATTACK;
    private String name = "AttackComponent";
    private double originDamage;
    private double damage;
    private int targetStrategy;
    private double originRange;
    private double range;
    private double originSpeed;
    private double speed;
    private double countdown;
    private List<EffectComponent> effects = new ArrayList<>();

    public AttackComponent(double damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects) {
        super(GameConfig.COMPONENT_ID.ATTACK);
        this.reset(damage, targetStrategy, range, speed, countdown, effects);
    }

    public void reset(double damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects) {
        this.originDamage = damage;
        this.damage = damage;
        this.targetStrategy = targetStrategy;
        this.range = range;
        this.speed = speed;
        this.originSpeed = speed;
        this.countdown = countdown;
        this.effects.clear();
        if (effects != null) {
            for (EffectComponent effect : effects)
                this.effects.add(effect);
        }
        //  System.out.println("countdown "+this.countdown);
        this.effects.add(new DamageEffect(this.damage));
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
        for (int i = 0; i < this.effects.size(); i++) {
            DamageEffect effect = (DamageEffect) this.effects.get(i);
            if (effect.getTypeID() == GameConfig.COMPONENT_ID.DAMAGE_EFFECT) {
                effect.setDamage(this.damage);
            }
        }
    }

    public AttackComponent clone() {
        try {
            return ComponentFactory.getInstance().createAttackComponent(this.damage, this.targetStrategy, this.range, this.speed, this.countdown, this.effects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getOriginDamage() {
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

    public void setRange(double range) {
        this.range = range;
    }

    public double getRange() {
        return this.range;
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

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getOriginSpeed() {
        return originSpeed;
    }

    public double getOriginRange() {
        return originRange;
    }

    public void setOriginRange(double originRange) {
        this.originRange = originRange;
    }
}