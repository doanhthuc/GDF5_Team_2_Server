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
    private double bulletSpeed;
    private double bulletRadius;

    private List<EffectComponent> effects = new ArrayList<>();

    public AttackComponent(double damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects, double bulletSpeed, double bulletRadius) {
        super(GameConfig.COMPONENT_ID.ATTACK);
        this.reset(damage, targetStrategy, range, speed, countdown, effects, bulletSpeed, bulletRadius);
    }

    public void reset(double damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects, double bulletSpeed, double bulletRadius) {
        this.originDamage = damage;
        this.damage = damage;
        this.targetStrategy = targetStrategy;
        this.originRange = range;
        this.range = range;
        this.speed = speed;
        this.originSpeed = speed;
        this.countdown = countdown;
        this.effects.clear();
        if (effects != null) {
            this.effects.addAll(effects);
        }
        this.effects.add(new DamageEffect(this.damage));
        this.bulletSpeed = bulletSpeed;
        this.bulletRadius = bulletRadius;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
        for (int i = 0; i < this.effects.size(); i++) {
            if (effects.get(i).getTypeID() == GameConfig.COMPONENT_ID.DAMAGE_EFFECT) {
                DamageEffect damageEffect = (DamageEffect) effects.get(i);
                damageEffect.setDamage(this.damage);
            }
        }
    }

    public AttackComponent clone(ComponentFactory componentFactory) {
        try {
            return componentFactory.createAttackComponent(this.damage, this.targetStrategy, this.range, this.speed, this.countdown, this.effects, this.bulletSpeed, this.bulletRadius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAttackStatistic(double damage, double range, double attackSpeed, List<EffectComponent> effects, double bulletSpeed, double bulletRadius) {
        this.damage = damage;
        this.originDamage = damage;
        this.range = range;
        this.originRange = range;
        this.speed = attackSpeed;
        this.originSpeed = attackSpeed;
        this.bulletSpeed = bulletSpeed;
        this.bulletRadius = bulletRadius;
        this.effects.clear();
        if (effects != null) {
            this.effects.addAll(effects);
        }
        this.effects.add(new DamageEffect(this.damage));
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

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public double getBulletRadius() {
        return bulletRadius;
    }

    public void setBulletRadius(double bulletRadius) {
        this.bulletRadius = bulletRadius;
    }
}