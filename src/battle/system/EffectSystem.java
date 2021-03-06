package battle.system;

import battle.Battle;
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

public class EffectSystem extends SystemECS {
    int id = GameConfig.SYSTEM_ID.EFFECT;
    public String name = "EffectSystem";

    public EffectSystem() {
        super(GameConfig.SYSTEM_ID.EFFECT);
        java.lang.System.out.println("new EffectSystem");
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        this.handleBuffAttackRangeEffect(battle);
        this.handleBuffAttackSpeedEffect(tick, battle);
        this.handleBuffAttackDamageEffect(tick, battle);
        this.handleDamageEffect(tick, battle);
        this.handleSlowEffect(tick, battle);
        this.handleFrozenEffect(tick, battle);
    }

    private void handleBuffAttackSpeedEffect(double tick, Battle battle) {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity : entityList) {
            AttackComponent attackComponent = (AttackComponent) entity.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            BuffAttackSpeedEffect buffAttackSpeedComponent = (BuffAttackSpeedEffect) entity.getComponent(GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED);

            attackComponent.setSpeed(attackComponent.getOriginSpeed() * (1 - (buffAttackSpeedComponent.getPercent() - 1)));
        }
    }

    private void handleBuffAttackDamageEffect(double tick, Battle battle) {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity : entityList) {
            AttackComponent attackComponent = (AttackComponent) entity.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            BuffAttackDamageEffect buffAttackDamageEffect = (BuffAttackDamageEffect) entity.getComponent(GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED);

            attackComponent.setDamage(attackComponent.getDamage() +
                    attackComponent.getOriginDamage() * buffAttackDamageEffect.getPercent());
        }
    }

    private void handleDamageEffect(double tick, Battle battle) {
        List<Integer> damageEffectID = new ArrayList<>();
        damageEffectID.add(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
        List<EntityECS> damagedEntity = battle.getEntityManager().getEntitiesHasComponents(damageEffectID);

        for (EntityECS entity : damagedEntity) {
            LifeComponent life = (LifeComponent) entity.getComponent(GameConfig.COMPONENT_ID.LIFE);
            if (life != null) {
                DamageEffect damageEffect = (DamageEffect) entity.getComponent(GameConfig.COMPONENT_ID.DAMAGE_EFFECT);
                life.setHp(life.getHp() - damageEffect.getDamage());
                entity.removeComponent(damageEffect, battle.getComponentManager());
            }
        }
    }

    private void handleFrozenEffect(double tick, Battle battle) {
        List<Integer> componentIdList = Arrays.asList(FrozenEffect.typeID);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity : entityList) {
            VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.typeID);
            FrozenEffect frozenComponent = (FrozenEffect) entity.getComponent(FrozenEffect.typeID);

            frozenComponent.setCountdown(frozenComponent.getCountdown() - tick / 1000);
            if (frozenComponent.getCountdown() <= 0) {
                entity.removeComponent(frozenComponent, battle.getComponentManager());
                this.updateOriginVelocity(velocityComponent);
            } else {
                velocityComponent.setSpeedX(0);
                velocityComponent.setSpeedY(0);
            }
        }
    }

    private void handleSlowEffect(double tick, Battle battle) {
        List<Integer> componentIdList = Arrays.asList(SlowEffect.typeID);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity : entityList) {
            VelocityComponent velocityComponent = (VelocityComponent) entity.getComponent(VelocityComponent.typeID);
            SlowEffect slowComponent = (SlowEffect) entity.getComponent(SlowEffect.typeID);
            slowComponent.setCountdown(slowComponent.getCountdown() - tick / 1000);
            if (slowComponent.getCountdown() <= 0) {
                this.updateOriginVelocity(velocityComponent);
                entity.removeComponent(slowComponent, battle.getComponentManager());
            } else {
                velocityComponent.setSpeedX(velocityComponent.getOriginSpeedX() * slowComponent.getPercent());
                velocityComponent.setSpeedY(velocityComponent.getOriginSpeedY() * slowComponent.getPercent());
            }
        }
    }

    private void handleBuffAttackRangeEffect(Battle battle) {
        List<Integer> componentIdList = Arrays.asList(
                GameConfig.COMPONENT_ID.BUFF_ATTACK_RANGE, GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> entityList = battle.getEntityManager().getEntitiesHasComponents(componentIdList);
        for (EntityECS entity : entityList) {
            AttackComponent attackComponent = (AttackComponent) entity.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            BuffAttackRangeEffect buffAttackRangeEffect = (BuffAttackRangeEffect) entity.getComponent(GameConfig.COMPONENT_ID.BUFF_ATTACK_RANGE);

            attackComponent.setRange(attackComponent.getOriginRange() + attackComponent.getOriginRange() * buffAttackRangeEffect.getPercent());
        }
    }

    private void updateOriginVelocity(VelocityComponent velocityComponent) {
        velocityComponent.setSpeedX(velocityComponent.getOriginSpeedX());
        velocityComponent.setOriginSpeedY(velocityComponent.getSpeedY());
    }
}
