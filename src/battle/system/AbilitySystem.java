package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.*;
import battle.component.effect.BuffAttackDamageEffect;
import battle.component.effect.BuffAttackSpeedEffect;
import battle.component.effect.TowerAbilityComponent;
import battle.component.info.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;

import java.util.Collections;
import java.util.List;

public class AbilitySystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.ABILITY;
    public String name = "AbilitySystem";

    public AbilitySystem() {
        super(GameConfig.SYSTEM_ID.ABILITY);
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        this.handleUnderGroundComponent(tick, battle);
        try {
            this.handleSpawnMinionComponent(tick, battle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.handleHealingAbility(tick, battle);
        this.handleBuffAbility(tick, battle);
    }

    private void handleUnderGroundComponent(double tick, Battle battle) {
        List<EntityECS> underGroundList = battle.getEntityManager()
                .getEntitiesHasComponents(Collections
                        .singletonList(GameConfig.COMPONENT_ID.UNDER_GROUND));
        for (EntityECS underGround : underGroundList) {
            LifeComponent lifeComponent = (LifeComponent) underGround.getComponent(GameConfig.COMPONENT_ID.LIFE);
            UnderGroundComponent underGroundComponent = (UnderGroundComponent) underGround.getComponent(GameConfig.COMPONENT_ID.UNDER_GROUND);
            PositionComponent positionComponent = (PositionComponent) underGround.getComponent(GameConfig.COMPONENT_ID.POSITION);
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

    //TODO: continue Implementing when have entity Factory
    private void handleSpawnMinionComponent(double tick, Battle battle) throws Exception {
        List<EntityECS> entityList = battle.getEntityManager()
                .getEntitiesHasComponents(Collections
                        .singletonList(GameConfig.COMPONENT_ID.SPAWN_MINION));
        for (EntityECS entity : entityList) {
            SpawnMinionComponent spawnMinionComponent = (SpawnMinionComponent) entity.getComponent(GameConfig.COMPONENT_ID.SPAWN_MINION);
            if (spawnMinionComponent.getPeriod() >= 0) {
                spawnMinionComponent.setPeriod(spawnMinionComponent.getPeriod() - tick / 1000);
            } else {
                spawnMinionComponent.setPeriod(2);
                PositionComponent positionComponent = (PositionComponent) entity.getComponent(GameConfig.COMPONENT_ID.POSITION);
                int spawnAmount = spawnMinionComponent.getSpawnAmount();
                if (spawnAmount < 5) {
                    battle.getEntityFactory().createDemonTreeMinion(
                            new Point(positionComponent.getX(), positionComponent.getY()), entity.getMode());
                    spawnMinionComponent.setSpawnAmount(spawnAmount + 1);
                }
            }
        }
    }

    private void handleHealingAbility(double tick, Battle battle) {
        List<EntityECS> entityList = battle.getEntityManager()
                .getEntitiesHasComponents(Collections
                        .singletonList(GameConfig.COMPONENT_ID.HEALING_ABILITY));
        List<EntityECS> monsterList = null;
        if (entityList.size() > 0) {
            monsterList = battle.getEntityManager()
                    .getEntitiesHasComponents(Collections
                            .singletonList(GameConfig.COMPONENT_ID.MONSTER_INFO));
        }
        for (EntityECS satyr : entityList) {
            HealingAbilityComponent healingAbilityComponent = (HealingAbilityComponent) satyr
                    .getComponent(GameConfig.COMPONENT_ID.HEALING_ABILITY);
            double countdown = healingAbilityComponent.getCountdown();
            if (countdown > 0) {
                healingAbilityComponent.setCountdown(countdown - tick / 1000);
            } else {
                for (EntityECS monster : monsterList) {
                    healingAbilityComponent.setCountdown(1);
                    if (monster.getActive() && monster.getMode() == satyr.getMode()) {
                        double distance = distanceFrom(satyr, monster);
                        if (distance <= healingAbilityComponent.getRange()) {
                            LifeComponent lifeComponent = (LifeComponent) monster.getComponent(GameConfig.COMPONENT_ID.LIFE);
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

    private void handleBuffAbility(double tick, Battle battle) {
        List<EntityECS> buffTowerList = battle.getEntityManager()
                .getEntitiesHasComponents(Collections
                        .singletonList(GameConfig.COMPONENT_ID.TOWER_ABILITY));
        List<EntityECS> damageTowerList = null;
        if (buffTowerList.size() > 0) {
            damageTowerList = battle.getEntityManager().getEntitiesHasComponents(Collections.singletonList(AttackComponent.typeID));
        }
        for (EntityECS buffTower : buffTowerList) {
            TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) buffTower.getComponent(TowerAbilityComponent.typeId);
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
        PositionComponent towerPositionComponent = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);
        PositionComponent monsterPositionComponent = (PositionComponent) monster.getComponent(GameConfig.COMPONENT_ID.POSITION);
        Point towerPoint = new Point(towerPositionComponent.getX(), towerPositionComponent.getY());
        Point monsterPoint = new Point(monsterPositionComponent.getX(), monsterPositionComponent.getY());
        return Utils.euclidDistance(towerPoint, monsterPoint);
    }
}
