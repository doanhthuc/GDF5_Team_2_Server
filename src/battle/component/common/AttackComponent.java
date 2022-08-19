package battle.component.common;

import battle.component.effect.DamageEffect;
import battle.component.effect.EffectComponent;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;
import battle.tick.TickManager;

import java.nio.ByteBuffer;
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
    private boolean canTargetAirMonster = true;
    private TickManager tickManager;
    private List<EffectComponent> effects = new ArrayList<>();
    private int _latestTick;

    public AttackComponent(double damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects, double bulletSpeed, double bulletRadius, boolean canTargetAirMonster) {
        super(GameConfig.COMPONENT_ID.ATTACK);
        this.reset(damage, targetStrategy, range, speed, countdown, effects, bulletSpeed, bulletRadius, canTargetAirMonster);
    }

    public void reset(double damage, int targetStrategy, double range, double speed, double countdown, List<EffectComponent> effects, double bulletSpeed, double bulletRadius, boolean canTargetAirMonster) {
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
        this._latestTick = -1;
        this.canTargetAirMonster = canTargetAirMonster;
    }

    public double getDamage() {
        int latestUpdateTick = tickManager.getCurrentTick();
        if (latestUpdateTick != this._latestTick) {
            this._latestTick = latestUpdateTick;
            this.speed = this.originSpeed;
            this.damage = this.originDamage;
        }
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
            return componentFactory.createAttackComponent(this.damage, this.targetStrategy, this.range, this.speed, this.countdown, this.effects, this.bulletSpeed, this.bulletRadius, this.canTargetAirMonster);
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
        int latestUpdateTick = tickManager.getCurrentTick();
        if (latestUpdateTick != this._latestTick) {
            this._latestTick = latestUpdateTick;
            this.speed = this.originSpeed;
            this.damage = this.originDamage;
        }
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

    public void addEffect(EffectComponent effect) {
        this.effects.add(effect);
    }

    public String getName() {
        return name;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

    public void setTickManager(TickManager tickManager) {
        this.tickManager = tickManager;
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

    public void setCanTargetAirMonster(boolean canTargetAirMonster) {
        this.canTargetAirMonster = canTargetAirMonster;
    }

    public boolean canTargetAirMonster() {
        return this.canTargetAirMonster;
    }

    public void setOriginDamage(double originDamage) {
        this.originDamage = originDamage;
    }

    public void setOriginSpeed(double originSpeed) {
        this.originSpeed = originSpeed;
    }

    @Override
    public void createData(ByteBuffer bf) {
        super.createData(bf);
    }
}