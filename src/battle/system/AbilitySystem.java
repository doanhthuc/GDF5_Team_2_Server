package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.*;
import battle.component.effect.BuffAttackDamageEffect;
import battle.component.effect.BuffAttackSpeedEffect;
import battle.component.effect.FrozenEffect;
import battle.component.effect.TowerAbilityComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AbilitySystem extends SystemECS {
    private static final String SYSTEM_NAME = "AbilitySystem";

    public AbilitySystem(long id) {
        super(GameConfig.SYSTEM_ID.ABILITY, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        this.tick = this.tick / 1000;
        this.handleUnderGroundComponent(tick, battle);
        try {
            this.handleSpawnMinionComponent(tick, battle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.handleHealingAbility(tick, battle);
//        this.handleBuffAbility(tick, battle);
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == MonsterInfoComponent.typeID;
    }

    private void handleUnderGroundComponent(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS entity = mapElement.getValue();
            if (!entity._hasComponent(UnderGroundComponent.typeID)) continue;;

            LifeComponent lifeComponent = (LifeComponent) entity.getComponent(LifeComponent.typeID);
            UnderGroundComponent underGroundComponent = (UnderGroundComponent) entity.getComponent(UnderGroundComponent.typeID);
            PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.typeID);

            if (entity._hasComponent(FrozenEffect.typeID)) {
                FrozenEffect frozenEffect = (FrozenEffect) entity.getComponent(FrozenEffect.typeID);
                if (frozenEffect.getCountdown() > 0) continue;
            }

            if (positionComponent != null) {
                if (!underGroundComponent.isInGround()) {
                    if (((lifeComponent.getHp() / lifeComponent.getMaxHP()) <= 0.7 - 0.3 * underGroundComponent.getTrigger())) {
                        underGroundComponent.setTrigger(underGroundComponent.getTrigger() + 1);
                        underGroundComponent.setDisableMoveDistance(positionComponent.getMoveDistance() + GameConfig.TILE_WIDTH * 3);
                        underGroundComponent.setInGround(true);
                    }
                } else {
                    if (underGroundComponent.getDisableMoveDistance() <= positionComponent.getMoveDistance()) {
                        underGroundComponent.setInGround(false);
                    }
                }
            }
        }
    }

    //TODO: continue Implementing when have entity Factory
    private void handleSpawnMinionComponent(double tick, Battle battle) throws Exception {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS entity = mapElement.getValue();
            if (!entity._hasComponent(SpawnMinionComponent.typeID)) continue;;

            SpawnMinionComponent spawnMinionComponent = (SpawnMinionComponent) entity.getComponent(SpawnMinionComponent.typeID);

            if (spawnMinionComponent.getPeriod() >= 0) {
                spawnMinionComponent.setPeriod(spawnMinionComponent.getPeriod() - tick);
            } else {
                spawnMinionComponent.setPeriod(2);
                PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.typeID);

                if (spawnMinionComponent.getSpawnAmount() < spawnMinionComponent.getMaxAmount()) {
                    battle.getEntityFactory().createDemonTreeMinion(
                            new Point(positionComponent.getX(), positionComponent.getY()), entity.getMode());
                    spawnMinionComponent.setSpawnAmount(spawnMinionComponent.getSpawnAmount() + 1);
                }
            }
        }
    }

    private void handleHealingAbility(double tick, Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS satyr = mapElement.getValue();

            if (!satyr._hasComponent(HealingAbilityComponent.typeID)) continue;
            if (!satyr._hasComponent(PositionComponent.typeID)) continue;

            HealingAbilityComponent healingAbilityComponent = (HealingAbilityComponent) satyr
                    .getComponent(HealingAbilityComponent.typeID);

            double countdown = healingAbilityComponent.getCountdown();
            if (countdown > 0) {
                healingAbilityComponent.setCountdown(countdown - tick);
            } else {
                healingAbilityComponent.setCountdown(1);
                for (Map.Entry<Long, EntityECS> mapElement2 : this.getEntityStore().entrySet()) {
                    EntityECS monster = mapElement2.getValue();
                    if (!monster._hasComponent(PositionComponent.typeID)) continue;

                    if (monster.getActive() && monster.getMode() == satyr.getMode()) {
                        PositionComponent monsterPos = (PositionComponent) monster.getComponent(PositionComponent.typeID);

                        if (monsterPos != null) {
                            double distance = distanceFrom(satyr, monster);

                            if (distance <= healingAbilityComponent.getRange()) {
                                LifeComponent lifeComponent = (LifeComponent) monster.getComponent(LifeComponent.typeID);

                                double entityHpAfterHeal = Math.min(
                                        lifeComponent.getHp() + lifeComponent.getMaxHP() * healingAbilityComponent.getHealingRate(),
                                        lifeComponent.getMaxHP());
                                lifeComponent.setHp(entityHpAfterHeal);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleBuffAbility(double tick, Battle battle) {
        List<EntityECS> buffTowerList = battle.getEntityManager()
                .getEntitiesHasComponents(Collections
                        .singletonList(TowerAbilityComponent.typeID));
        List<EntityECS> damageTowerList = null;
        if (buffTowerList.size() > 0) {
            damageTowerList = battle.getEntityManager().getEntitiesHasComponents(Collections.singletonList(AttackComponent.typeID));
        }
        for (EntityECS buffTower : buffTowerList) {
            TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) buffTower.getComponent(TowerAbilityComponent.typeID);
            for (EntityECS damageTower : damageTowerList) {
                if (this.distanceFrom(buffTower, damageTower) < towerAbilityComponent.getRange()) {
                    int typeId = towerAbilityComponent.getEffect().getTypeID();
                    if (typeId == BuffAttackDamageEffect.typeID) {
                        BuffAttackDamageEffect buffAttackDamageEffect = (BuffAttackDamageEffect) towerAbilityComponent.getEffect();
                        AttackComponent attackComponent = (AttackComponent) damageTower.getComponent(AttackComponent.typeID);
                        attackComponent.setDamage(attackComponent.getDamage() + attackComponent.getOriginDamage() * buffAttackDamageEffect.getPercent());
                    } else if (typeId == BuffAttackSpeedEffect.typeID) {
                        BuffAttackSpeedEffect buffAttackSpeedEffect = (BuffAttackSpeedEffect) towerAbilityComponent.getEffect();
                        AttackComponent attackComponent = (AttackComponent) damageTower.getComponent(AttackComponent.typeID);
                        attackComponent.setSpeed(attackComponent.getSpeed() - (attackComponent.getOriginSpeed() * buffAttackSpeedEffect.getPercent()));
                    }
                }

            }
        }


    }

    private double distanceFrom(EntityECS tower, EntityECS monster) {
        PositionComponent towerPositionComponent = (PositionComponent) tower.getComponent(PositionComponent.typeID);
        PositionComponent monsterPositionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
        Point towerPoint = new Point(towerPositionComponent.getX(), towerPositionComponent.getY());
        Point monsterPoint = new Point(monsterPositionComponent.getX(), monsterPositionComponent.getY());
        return Utils.euclidDistance(towerPoint, monsterPoint);
    }
}
