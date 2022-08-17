package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.AttackComponent;
import battle.component.common.Component;
import battle.component.common.PositionComponent;
import battle.component.common.UnderGroundComponent;
import battle.component.effect.EffectComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AttackSystem extends SystemECS {
    private static final String SYSTEM_NAME = "AttackSystem";

    public AttackSystem(long id) {
        super(GameConfig.SYSTEM_ID.ATTACK, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) throws Exception {
        this.tick = this.getElapseTime();
        //AbilitySystem have monster
        SystemECS abilitySystem = battle.abilitySystem;
        //
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS tower = mapElement.getValue();

            AttackComponent attackComponent = (AttackComponent) tower.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            double countDown = attackComponent.getCountdown();
            if (countDown > 0) {
                attackComponent.setCountdown(countDown - tick / 1000);
            }
            if (countDown <= 0) {
                List<EntityECS> monsterInRange = new ArrayList<>();

                for (Map.Entry<Long, EntityECS> monsterElement : abilitySystem.getEntityStore().entrySet()) {
                    EntityECS monster = monsterElement.getValue();
                    if (!monster._hasComponent(PositionComponent.typeID)) continue;
                    MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);
                    if (monsterInfoComponent.getClasss().equals(GameConfig.MONSTER.CLASS.AIR) && !attackComponent.canTargetAirMonster())
                        continue;
                    UnderGroundComponent underGroundComponent = (UnderGroundComponent) monster.getComponent(UnderGroundComponent.typeID);
                    if (underGroundComponent == null || (!underGroundComponent.isInGround())) {
                        if (monster.getActive() && monster.getMode() == tower.getMode()) {
                            double distance = this._distanceFrom(tower, monster);
                            if (distance <= attackComponent.getRange()) monsterInRange.add(monster);
                        }
                    }
                }

                if (monsterInRange.size() > 0) {
                    EntityECS targetMonster = this.findTargetMonsterByStrategy(tower, attackComponent.getTargetStrategy(), monsterInRange);
                    if (targetMonster != null) {
                        PositionComponent monsterPos = (PositionComponent) targetMonster.getComponent(PositionComponent.typeID);
                        PositionComponent towerPos = (PositionComponent) tower.getComponent(PositionComponent.typeID);
                        List<EffectComponent> cloneEffect = new ArrayList<>();
                        //clone AttackComponent Effect to bulletEffect
                        for (EffectComponent effect : attackComponent.getEffects())
                            cloneEffect.add(effect.clone(battle.getComponentFactory()));

                        try {
                            if (tower.getTypeID() == GameConfig.ENTITY_ID.FROG_TOWER) {
                                double distance = this._distanceFrom(tower, targetMonster);
                                double k = attackComponent.getRange() / distance;
                                PositionComponent destination = new PositionComponent(k * (monsterPos.getX() - towerPos.getX()) + towerPos.getX(), k * (monsterPos.getY() - towerPos.getY()) + towerPos.getY());
                                battle.getEntityFactory().createBullet(tower.getTypeID(), towerPos, null, destination.getPos(), cloneEffect, tower.getMode(), attackComponent.getBulletSpeed(), attackComponent.getBulletRadius());
                            } else {
                                battle.getEntityFactory().createBullet(tower.getTypeID(), towerPos, targetMonster, monsterPos.getPos(), cloneEffect, tower.getMode(), attackComponent.getBulletSpeed(), attackComponent.getBulletRadius());
                            }
                        } catch (Exception e) {

                        }
                        attackComponent.setCountdown(attackComponent.getSpeed());
                    }
                }
            }

        }

    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == AttackComponent.typeID;
    }

    public double _distanceFrom(EntityECS tower, EntityECS monster) {
        PositionComponent towerPos = (PositionComponent) tower.getComponent(PositionComponent.typeID);
        PositionComponent monsterPos = (PositionComponent) monster.getComponent(PositionComponent.typeID);
        return Utils.euclidDistance(new Point(towerPos.getX(), towerPos.getY()), new Point(monsterPos.getX(), monsterPos.getY()));
    }

    public EntityECS findTargetMonsterByStrategy(EntityECS tower, int strategy, List<EntityECS> monsterInRange) {
        for (EntityECS monster : monsterInRange) {
            if (monster.getTypeID() == GameConfig.ENTITY_ID.DARK_GIANT) {
                return monster;
            }
        }

        EntityECS targetMonster = null;
        switch (strategy) {
            case GameConfig.TOWER_TARGET_STRATEGY.MAX_HP: {
                double maxHP = -1;
                int maxHPIndex = -1;
                for (int i = 0; i < monsterInRange.size(); i++) {
                    LifeComponent monsterLife = (LifeComponent) monsterInRange.get(i).getComponent(GameConfig.COMPONENT_ID.LIFE);
                    double hp = monsterLife.getHp();
                    if (hp > maxHP) {
                        maxHP = hp;
                        maxHPIndex = i;
                    }
                }
                if (maxHPIndex != -1) targetMonster = monsterInRange.get(maxHPIndex);
                break;
            }
            case GameConfig.TOWER_TARGET_STRATEGY.MIN_HP:
                double minHP = Double.MAX_VALUE;
                int minHPIndex = -1;
                for (int i = 0; i < monsterInRange.size(); i++) {
                    LifeComponent monsterLife = (LifeComponent) monsterInRange.get(i).getComponent(GameConfig.COMPONENT_ID.LIFE);
                    double hp = monsterLife.getHp();
                    if (hp < minHP) {
                        minHP = hp;
                        minHPIndex = i;
                    }
                }
                if (minHPIndex != -1) targetMonster = monsterInRange.get(minHPIndex);
                break;
            case GameConfig.TOWER_TARGET_STRATEGY.MAX_DISTANCE:
                double maxDistance = -1;
                int maxDistanceIndex = -1;
                for (int i = 0; i < monsterInRange.size(); i++) {
                    double distance = this._distanceFrom(monsterInRange.get(i), tower);
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        maxDistanceIndex = i;
                    }
                }
                if (maxDistanceIndex != -1) targetMonster = monsterInRange.get(maxDistanceIndex);
                break;
            case GameConfig.TOWER_TARGET_STRATEGY.MIN_DISTANCE:
                double minDistance = Double.MAX_VALUE;
                int minDistanceIndex = -1;
                for (int i = 0; i < monsterInRange.size(); i++) {
                    double distance = this._distanceFrom(monsterInRange.get(i), tower);
                    if (distance < minDistance) {
                        minDistance = distance;
                        minDistanceIndex = i;
                    }
                }
                if (minDistanceIndex != -1) targetMonster = monsterInRange.get(minDistanceIndex);
                break;
            default:
                throw new Error("Invalid strategy");
        }
        return targetMonster;
    }
}
