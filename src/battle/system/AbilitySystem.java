package battle.system;

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

public class AbilitySystem extends SystemECS implements Runnable {
    public int id = GameConfig.SYSTEM_ID.ABILITY;
    public String name = "AbilitySystem";

    public AbilitySystem() {
        super(GameConfig.SYSTEM_ID.ABILITY);
    }

    @Override
    public void run() {
        this.tick = this.getElapseTime();
        this.handleUnderGroundComponent(tick);
        try {
            this.handleSpawnMinionComponent(tick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.handleHealingAbility(tick);
        this.handleBuffAbility(tick);
    }

    private void handleUnderGroundComponent(double tick) {
        List<EntityECS> underGroundList = EntityManager.getInstance()
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
    private void handleSpawnMinionComponent(double tick) throws Exception {
        List<EntityECS> entityList = EntityManager.getInstance()
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
                    EntityFactory.getInstance().createDemonTreeMinion(
                            new Point(positionComponent.getX(), positionComponent.getY()), entity.getMode());
                    spawnMinionComponent.setSpawnAmount(spawnAmount + 1);
                }
            }
        }
    }

    private void handleHealingAbility(double tick) {
        List<EntityECS> entityList = EntityManager.getInstance()
                .getEntitiesHasComponents(Collections
                        .singletonList(GameConfig.COMPONENT_ID.HEALING_ABILITY));
        List<EntityECS> monsterList = null;
        if (entityList.size() > 0) {
            monsterList = EntityManager.getInstance()
                    .getEntitiesHasComponents(Collections
                            .singletonList(GameConfig.COMPONENT_ID.MONSTER_INFO));
        }
        for (EntityECS satyr : entityList) {
            HealingAbilityComponent healingAbilityComponent = (HealingAbilityComponent) satyr
                    .getComponent(GameConfig.COMPONENT_ID.HEALING_ABILITY);
            if (healingAbilityComponent.getCountdown() > 0) {
                for (EntityECS monster : monsterList) {
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

    private void handleBuffAbility(double tick) {
        List<EntityECS> buffTowerList = EntityManager.getInstance()
                .getEntitiesHasComponents(Collections
                        .singletonList(GameConfig.COMPONENT_ID.TOWER_ABILITY));
        List<EntityECS> damageTowerList = null;
        if (buffTowerList.size() > 0) {
            damageTowerList = EntityManager.getInstance()
                    .getEntitiesHasComponents(
                            Collections.singletonList(GameConfig.COMPONENT_ID.TOWER_ABILITY));
        }
        for (EntityECS buffTower : buffTowerList) {
            TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) buffTower.getComponent(GameConfig.COMPONENT_ID.TOWER_ABILITY);
            for (EntityECS damageTower : damageTowerList) {
                if (this.distanceFrom(buffTower, damageTower) < towerAbilityComponent.getRange()) {
                    int typeId = towerAbilityComponent.getEffect().getTypeID();
                    if (typeId == GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE) {
                        BuffAttackDamageEffect buffAttackDamageEffect = (BuffAttackDamageEffect) towerAbilityComponent.getEffect();
                        AttackComponent attackComponent = (AttackComponent) damageTower.getComponent(GameConfig.COMPONENT_ID.ATTACK);
                        attackComponent.setDamage(attackComponent.getDamage() + attackComponent.getOriginDamage() * buffAttackDamageEffect.getPercent());
                    } else if (typeId == GameConfig.COMPONENT_ID.BUFF_ATTACK_SPEED) {
                        BuffAttackSpeedEffect buffAttackSpeedEffect = (BuffAttackSpeedEffect) towerAbilityComponent.getEffect();
                        AttackComponent attackComponent = (AttackComponent) damageTower.getComponent(GameConfig.COMPONENT_ID.ATTACK);
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
