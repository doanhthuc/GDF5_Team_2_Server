package battle.system;

import battle.component.common.AttackComponent;
import battle.component.common.VelocityComponent;
import battle.component.effect.*;
import battle.component.info.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EffectSystem extends SystemECS implements Runnable {
    int id = GameConfig.SYSTEM_ID.EFFECT;
    public String name = "EffectSystem";

    public EffectSystem() {
        super(GameConfig.SYSTEM_ID.EFFECT);
        java.lang.System.out.println("new EffectSystem");
    }

    @Override
    public void run() {
        this.tick = this.getElapseTime();
        java.lang.System.out.println(this.tick);
        this.handleBuffAttackRangeEffect();
        this.handleBuffAttackSpeedEffect(tick);
        this.handleBuffAttackDamageEffect(tick);
        this.handleDamageEffect(tick);
        this.handleSlowEffect(tick);
        this.handleFrozenEffect(tick);
    }

    private void handleBuffAttackSpeedEffect(double tick) {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity: entityList) {
            AttackComponent attackComponent = (AttackComponent) entity.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            BuffAttackSpeedEffect buffAttackSpeedComponent = (BuffAttackSpeedEffect) entity.getComponent(GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED);

            attackComponent.setSpeed(attackComponent.getOriginSpeed() * (1 - (buffAttackSpeedComponent.getPercent() - 1)));
        }
    }

    private void handleBuffAttackDamageEffect(double tick) {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity: entityList) {
            AttackComponent attackComponent = (AttackComponent) entity.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            BuffAttackDamageEffect buffAttackDamageEffect = (BuffAttackDamageEffect) entity.getComponent(GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED);

            attackComponent.setDamage(attackComponent.getDamage() +
                    attackComponent.getOriginDamage() * buffAttackDamageEffect.getPercent());
        }
    }

    private void handleDamageEffect(double tick) {
        List<Integer> damageEffectID = new ArrayList<>();
        damageEffectID.add(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        List<EntityECS> damagedEntity = EntityManager.getInstance().getEntitiesHasComponents(damageEffectID);

        for (EntityECS entity : damagedEntity) {
            LifeComponent life = (LifeComponent) entity.getComponent(GameConfig.COMPONENT_ID.LIFE);
            if (life != null) {
                DamageEffect damageEffect = (DamageEffect) entity.getComponent(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
                life.setHp(life.getHp() - damageEffect.getDamage());
                entity.removeComponent(damageEffect);
            }
        }
    }

    private void handleFrozenEffect(double tick) {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.FROZEN_EFFECT, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity: entityList) {
            VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(GameConfig.COMPONENT_ID.VELOCITY);
            FrozenEffect frozenComponent = (FrozenEffect) entity.getComponent(GameConfig.COMPONENT_ID.FROZEN_EFFECT);

            frozenComponent.setCountdown(frozenComponent.getCountdown() - tick);
            if (frozenComponent.getCountdown() <= 0) {
                entity.removeComponent(frozenComponent);
                this.updateOriginVelocity(velocityComponent);
            } else {
                velocityComponent.setSpeedX(0);
                velocityComponent.setSpeedY(0);
            }
        }
    }

    private void handleSlowEffect (double tick) {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.SLOW_EFFECT, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity: entityList) {
            VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(GameConfig.COMPONENT_ID.VELOCITY);
            SlowEffect slowComponent = (SlowEffect) entity.getComponent(GameConfig.COMPONENT_ID.SLOW_EFFECT);

            slowComponent.setCountdown(slowComponent.getCountdown() - tick);
            if (slowComponent.getCountdown() <= 0) {
                entity.removeComponent(slowComponent);
                this.updateOriginVelocity(velocityComponent);
            } else {
                velocityComponent.setSpeedX(velocityComponent.getOriginSpeedX() * slowComponent.getPercent());
                velocityComponent.setSpeedY(velocityComponent.getOriginSpeedY() * slowComponent.getPercent());
            }
        }
    }

    private void handleBuffAttackRangeEffect() {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.BUFF_ATTACK_RANGE, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity: entityList) {
            AttackComponent attackComponent = (AttackComponent) entity.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            BuffAttackRangeEffect buffAttackRangeEffect = (BuffAttackRangeEffect) entity.getComponent(GameConfig.COMPONENT_ID.BUFF_ATTACK_RANGE);

            attackComponent.setRange(attackComponent.getOriginRange() + attackComponent.getOriginRange() * buffAttackRangeEffect.getPercent());
        }
    }

    private void updateOriginVelocity (VelocityComponent velocityComponent) {
        velocityComponent.setSpeedX(velocityComponent.getOriginSpeedX());
        velocityComponent.setOriginSpeedY(velocityComponent.getSpeedY());
    }
}
