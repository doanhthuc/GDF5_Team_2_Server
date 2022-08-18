package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.*;
import battle.component.effect.*;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.towerskill.DamageAmplifyComponent;
import battle.component.towerskill.PoisonEffect;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.*;

public class EffectSystem extends SystemECS {
    private final static String SYSTEM_NAME = "EffectSystem";

    public EffectSystem(long id) {
        super(GameConfig.SYSTEM_ID.EFFECT, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        this.handleDamageEffect(tick, battle);
        this.handleSlowEffect(tick, battle);
        this.handleFrozenEffect(tick, battle);
        this.handleTrapEffect(tick, battle);
        this.handlePoisonEffect(tick, battle);
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == MonsterInfoComponent.typeID;
    }


    private void handleDamageEffect(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS monster = mapElement.getValue();
            if (!monster._hasComponent(DamageEffect.typeID)) continue;

            LifeComponent life = (LifeComponent) monster.getComponent(LifeComponent.typeID);
            if (life != null) {
                DamageEffect damageEffect = (DamageEffect) monster.getComponent(DamageEffect.typeID);
                if (monster._hasComponent(DamageAmplifyComponent.typeID)) {
                    DamageAmplifyComponent damageAmplify = (DamageAmplifyComponent) monster.getComponent(DamageAmplifyComponent.typeID);
                    life.setHp(life.getHp() - damageEffect.getDamage() * damageAmplify.getAmplifyRate());
                } else {
                    life.setHp(life.getHp() - damageEffect.getDamage());
                }
                monster.removeComponent(damageEffect);
            }
        }
    }

    private void handleFrozenEffect(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS monster = mapElement.getValue();
            if (!monster._hasComponent(FrozenEffect.typeID)) continue;

            VelocityComponent velocityComponent = (VelocityComponent) monster.getComponent(VelocityComponent.typeID);
            FrozenEffect frozenComponent = (FrozenEffect) monster.getComponent(FrozenEffect.typeID);

            frozenComponent.setCountdown(frozenComponent.getCountdown() - tick / 1000);

            if (frozenComponent.getCountdown() <= 0) {
                DamageAmplifyComponent damageAmplify = (DamageAmplifyComponent) monster.getComponent(DamageAmplifyComponent.typeID);
                if (damageAmplify != null) monster.removeComponent(damageAmplify);
                monster.removeComponent(frozenComponent);
                this.updateOriginVelocity(velocityComponent);
            } else {
                velocityComponent.setSpeedX(0);
                velocityComponent.setSpeedY(0);
            }
        }
    }

    private void handleSlowEffect(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS monster = mapElement.getValue();
            if (!monster._hasComponent(SlowEffect.typeID)) continue;

            VelocityComponent velocityComponent = (VelocityComponent) monster.getComponent(VelocityComponent.typeID);
            SlowEffect slowComponent = (SlowEffect) monster.getComponent(SlowEffect.typeID);

            slowComponent.setCountdown(slowComponent.getCountdown() - tick / 1000);
            if (slowComponent.getCountdown() <= 0) {

                this.updateOriginVelocity(velocityComponent);
                monster.removeComponent(slowComponent);
            } else {
                velocityComponent.setSpeedX(Math.min(velocityComponent.getOriginSpeedX() * slowComponent.getPercent(), velocityComponent.getSpeedX()));
                velocityComponent.setSpeedY(Math.min(velocityComponent.getOriginSpeedY() * slowComponent.getPercent(), velocityComponent.getSpeedY()));
            }
        }
    }

    private void handleTrapEffect(double tick, Battle battle) throws Exception {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS monster = mapElement.getValue();
            if (!monster._hasComponent(TrapEffect.typeID)) continue;

            TrapEffect trapEffect = (TrapEffect) monster.getComponent(GameConfig.COMPONENT_ID.TRAP_EFFECT);

            if (trapEffect.isExecuted()) {
                if (trapEffect.getCountdown() > 0) {
                    trapEffect.setCountdown(trapEffect.getCountdown() - tick / 1000);
                } else {
                    Point bornPos = Utils.tile2Pixel(GameConfig.MONSTER_BORN_POSITION.x, GameConfig.MONSTER_BORN_POSITION.y, monster.getMode());
                    PositionComponent newPos = battle.getComponentFactory().createPositionComponent(bornPos.x, bornPos.y);
                    monster.addComponent(newPos);

                    List<Point> path = battle.getEntityFactory().getShortestPathInTile(monster.getMode(),
                            (int) GameConfig.MONSTER_BORN_POSITION.x,
                            (int) GameConfig.MONSTER_BORN_POSITION.y);
                    PathComponent pathComponent = battle.getComponentFactory()
                            .createPathComponent(path, monster.getMode(), true);
                    monster.addComponent(pathComponent);

                    monster.removeComponent(trapEffect);
                }
            } else {
                PositionComponent pos = (PositionComponent) monster.getComponent(GameConfig.COMPONENT_ID.POSITION);

                PathComponent pathComponent = (PathComponent) monster.getComponent(GameConfig.COMPONENT_ID.PATH);

                pathComponent.setCurrentPathIDx(0);
                monster.removeComponent(pos);

                Point bornPos = Utils.tile2Pixel(GameConfig.MONSTER_BORN_POSITION.x,
                        GameConfig.MONSTER_BORN_POSITION.y, monster.getMode());
                double time = Utils.euclidDistance(pos, bornPos) / (2 * GameConfig.TILE_WIDTH);

                trapEffect.setCountdown(time + 0.5);
            }
        }
    }

    private void handlePoisonEffect(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS monster = mapElement.getValue();
            if (!monster._hasComponent(PoisonEffect.typeID)) continue;
            if (!monster._hasComponent(LifeComponent.typeID)) continue;

            PoisonEffect poisonEffect = (PoisonEffect) monster.getComponent(PoisonEffect.typeID);

            if (poisonEffect.getDuration() > 0) {
                poisonEffect.setDuration(poisonEffect.getDuration() - tick / 1000);
                LifeComponent lifeComponent = (LifeComponent) monster.getComponent(LifeComponent.typeID);
                lifeComponent.setHp(lifeComponent.getHp() - poisonEffect.getHealthPerSecond() * tick / 1000);
            } else {
                monster.removeComponent(poisonEffect);
            }
        }
    }

    private void updateOriginVelocity(VelocityComponent velocityComponent) {
        velocityComponent.setSpeedX(velocityComponent.getOriginSpeedX());
        velocityComponent.setSpeedY(velocityComponent.getOriginSpeedY());
    }
}
