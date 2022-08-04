package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.AttackComponent;
import battle.component.common.PositionComponent;
import battle.component.common.UnderGroundComponent;
import battle.component.info.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class AttackSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.ATTACK;
    public String name = "AttackSystem";

    public AttackSystem() {
        super(GameConfig.SYSTEM_ID.ATTACK);
//        java.lang.System.out.println("AttackSystem");
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        //Create List of Component TypeIDs
        List<Integer> typeIDTower = new ArrayList<>();
        typeIDTower.add(GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> towerList = battle.getEntityManager().getEntitiesHasComponents(typeIDTower);

        List<Integer> typeIDMonster = new ArrayList<>();
        typeIDMonster.add(GameConfig.COMPONENT_ID.MONSTER_INFO);
        List<EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(typeIDMonster);
//        Debug Bullet
//        List<Integer> typeIDBullet = new ArrayList<>();
//        typeIDBullet.add(GameConfig.COMPONENT_ID.BULLET_INFO);
//        List<EntityECS> bulletList = battle.getEntityManager().getEntitiesHasComponents(typeIDBullet);
//        for (EntityECS bullet : bulletList) {
//            bullet.toString();
//        }
        for (EntityECS tower : towerList) {
            AttackComponent attackComponent = (AttackComponent) tower.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            double countDown = attackComponent.getCountdown();
            if (countDown > 0) {
                attackComponent.setCountdown(countDown - tick * 1.0 / 1000);
            }
            if (countDown <= 0) {
                List<EntityECS> monsterInRange = new ArrayList<>();

                for (EntityECS monster : monsterList) {
                    UnderGroundComponent underGroundComponent = (UnderGroundComponent) monster.getComponent(GameConfig.COMPONENT_ID.UNDER_GROUND);
                    if (underGroundComponent == null || (underGroundComponent.isInGround() == false)) {
                        if (monster.getActive() && monster.getMode() == tower.getMode()) {
                            double distance = this._distanceFrom(tower, monster);
                            if (distance <= attackComponent.getRange()) monsterInRange.add(monster);
                        }
                    }
                }
                if (monsterInRange.size() > 0) {
                    EntityECS targetMonster = this.findTargetMonsterByStrategy(attackComponent.getTargetStrategy(), monsterInRange);
                    if (targetMonster != null) {
                        PositionComponent monsterPos = (PositionComponent) targetMonster.getComponent(GameConfig.COMPONENT_ID.POSITION);
                        PositionComponent towerPos = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);

                        try {
                            if (tower.getTypeID() == GameConfig.ENTITY_ID.FROG_TOWER) {
                                double distance = this._distanceFrom(tower, targetMonster);
                                double k = attackComponent.getRange() / distance;
                                PositionComponent destination = new PositionComponent(k * (monsterPos.getX() - towerPos.getX()) + towerPos.getX(), k * (monsterPos.getY() - towerPos.getY()) + towerPos.getY());
                                battle.getEntityFactory().createBullet(tower.getTypeID(), towerPos, destination, attackComponent.getEffects(), tower.getMode(), attackComponent.getBulletSpeed(), attackComponent.getBulletRadius());
                            } else {
                                battle.getEntityFactory().createBullet(tower.getTypeID(), towerPos, monsterPos, attackComponent.getEffects(), tower.getMode(), attackComponent.getBulletSpeed(), attackComponent.getBulletRadius());
                            }
                        } catch (Exception e) {

                        }
                        attackComponent.setCountdown(attackComponent.getSpeed());
                    }
                }
            }

        }

    }

    public double _distanceFrom(EntityECS tower, EntityECS monster) {
        PositionComponent towerPos = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);
        PositionComponent monsterPos = (PositionComponent) monster.getComponent(GameConfig.COMPONENT_ID.POSITION);
        // System.out.println("AttackSystem Position "+towerPos.getX()+" "+towerPos.getY()+" "+monsterPos.getX()+" "+monsterPos.getY());
        return Utils.euclidDistance(new Point(towerPos.getX(), towerPos.getY()), new Point(monsterPos.getX(), monsterPos.getY()));
    }

    public EntityECS findTargetMonsterByStrategy(int strategy, List<EntityECS> monsterInRange) {
        for (EntityECS monster : monsterInRange) {
            if (monster.getTypeID() == GameConfig.ENTITY_ID.DARK_GIANT) {
                return monster;
            }
        }

//        return monsterInRange.get(0);
        // TODO: Implement when have burrowed monster
        /*for (EntityECS monster: monsterInRange) {
            UnderGroundComponent underGroundComponent = (UnderGroundComponent) monster.
                    getComponent(GameConfig.COMPONENT_ID.UNDER_GROUND);
            if (underGroundComponent != null && underGroundComponent.isInGround()) {
                return monster;
            }
        }*/

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
                if (maxHPIndex!=-1) targetMonster = monsterInRange.get(maxHPIndex);
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
                if (minHPIndex!=-1) targetMonster = monsterInRange.get(minHPIndex);
                break;
            case GameConfig.TOWER_TARGET_STRATEGY.MAX_DISTANCE:
                double maxDistance = -1;
                int maxDistanceIndex = -1;
                for (int i = 0; i < monsterInRange.size(); i++) {
                    double distance = this._distanceFrom(monsterInRange.get(i), monsterInRange.get(i));
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        maxDistanceIndex = i;
                    }
                }
                if (maxDistanceIndex!=-1) targetMonster = monsterInRange.get(maxDistanceIndex);
                break;
            case GameConfig.TOWER_TARGET_STRATEGY.MIN_DISTANCE:
                double minDistance = Double.MAX_VALUE;
                int minDistanceIndex = -1;
                for (int i = 0; i < monsterInRange.size(); i++) {
                    double distance = this._distanceFrom(monsterInRange.get(i), monsterInRange.get(i));
                    if (distance < minDistance) {
                        minDistance = distance;
                        minDistanceIndex = i;
                    }
                }
                if (minDistanceIndex!=-1) targetMonster = monsterInRange.get(minDistanceIndex);
                break;
            default:
                throw new Error("Invalid strategy");
        }
        return targetMonster;
    }
}
