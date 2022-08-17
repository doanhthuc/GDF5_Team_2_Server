package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.*;
import battle.component.effect.*;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EffectSystem extends SystemECS {
    private final static String SYSTEM_NAME = "EffectSystem";

    public EffectSystem(long id) {
        super(GameConfig.SYSTEM_ID.EFFECT, SYSTEM_NAME,id);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        this.handleDamageEffect(tick, battle);
        this.handleSlowEffect(tick, battle);
        this.handleFrozenEffect(tick, battle);
        this.handleTrapEffect(tick, battle);
        //add _handlePoisonEffect()
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == MonsterInfoComponent.typeID;
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

    private void handleTrapEffect (double tick, Battle battle) throws Exception {
        List<Integer> componentIdList = Collections.singletonList(GameConfig.COMPONENT_ID.TRAP_EFFECT);
        List<EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(componentIdList);

        for (EntityECS entity: monsterList) {
            TrapEffect trapEffect = (TrapEffect) entity.getComponent(GameConfig.COMPONENT_ID.TRAP_EFFECT);

            if (trapEffect.isExecuted()) {
                if (trapEffect.getCountdown() > 0) {
                    trapEffect.setCountdown(trapEffect.getCountdown() - tick / 1000);
                } else {
                    Point bornPos = Utils.tile2Pixel(GameConfig.MONSTER_BORN_POSITION.x, GameConfig.MONSTER_BORN_POSITION.y, entity.getMode());
                    PositionComponent newPos = battle.getComponentFactory().createPositionComponent(bornPos.x, bornPos.y);
                    entity.addComponent(newPos);

                    List<Point> path = battle.getEntityFactory().getShortestPathInTile(entity.getMode(),
                            (int) GameConfig.MONSTER_BORN_POSITION.x,
                            (int) GameConfig.MONSTER_BORN_POSITION.y);
                    PathComponent pathComponent = battle.getComponentFactory()
                            .createPathComponent(path, entity.getMode(), true);

                    entity.addComponent(pathComponent);
                    entity.removeComponent(trapEffect, battle.getComponentManager());
                }
            } else {
                PositionComponent pos = (PositionComponent) entity.getComponent(GameConfig.COMPONENT_ID.POSITION);
                PathComponent pathComponent = (PathComponent) entity.getComponent(GameConfig.COMPONENT_ID.PATH);

                pathComponent.setCurrentPathIDx(0);
                entity.removeComponent(pos, battle.getComponentManager());

                Point bornPos = Utils.tile2Pixel(GameConfig.MONSTER_BORN_POSITION.x,
                        GameConfig.MONSTER_BORN_POSITION.y, entity.getMode());
                double time = Utils.euclidDistance(pos, bornPos) / (2 * GameConfig.TILE_WIDTH);

                trapEffect.setCountdown(time + 0.5);
            }
        }
    }

    private void updateOriginVelocity(VelocityComponent velocityComponent) {
        velocityComponent.setSpeedX(velocityComponent.getOriginSpeedX());
        velocityComponent.setOriginSpeedY(velocityComponent.getSpeedY());
    }
}
