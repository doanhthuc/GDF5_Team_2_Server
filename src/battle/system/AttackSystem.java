package battle.system;

import battle.common.Point;
import battle.common.Utils;
import battle.component.common.AttackComponent;
import battle.component.common.PositionComponent;
import battle.component.info.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class AttackSystem extends SystemECS implements Runnable {
    public int id = GameConfig.SYSTEM_ID.ATTACK;
    public String name = "AttackSystem";

    public AttackSystem() {
        super(GameConfig.SYSTEM_ID.ATTACK);
//        java.lang.System.out.println("AttackSystem");
    }

    @Override
    public void run() {
        this.tick = this.getElapseTime();
        //Create List of Component TypeIDs
        List<Integer> typeIDTower = new ArrayList<>();
        typeIDTower.add(GameConfig.COMPONENT_ID.ATTACK);
        List<EntityECS> towerList = EntityManager.getInstance().getEntitiesHasComponents(typeIDTower);

        List<Integer> typeIDMonster = new ArrayList<>();
        typeIDMonster.add(GameConfig.COMPONENT_ID.MONSTER_INFO);
        List<EntityECS> monsterList = EntityManager.getInstance().getEntitiesHasComponents(typeIDMonster);
//        Debug Bullet
//        List<Integer> typeIDBullet = new ArrayList<>();
//        typeIDBullet.add(GameConfig.COMPONENT_ID.BULLET_INFO);
//        List<EntityECS> bulletList = EntityManager.getInstance().getEntitiesHasComponents(typeIDBullet);
//        for (EntityECS bullet : bulletList) {
//            bullet.toString();
//        }
        for (EntityECS tower : towerList) {
            AttackComponent attackComponent = (AttackComponent) tower.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            double countDown = attackComponent.getCountdown();
            if (countDown > 0) {
                attackComponent.setCountdown(countDown - tick);
            }
            if (countDown <= 0) {
                List<EntityECS> monsterInRange = new ArrayList<>();
                for (EntityECS monster : monsterList) {
                    if (monster.getActive() && monster.getMode() == tower.getMode()) {
                        double distance = this._distanceFrom(tower, monster);
                        if (distance <= attackComponent.getRange()) monsterInRange.add(monster);
                    }
                }
                if (monsterInRange.size() > 0) {
                    EntityECS targetMonster = this.findTargetMonsterByStrategy(attackComponent.getTargetStrategy(), monsterInRange);
                    if (targetMonster != null) {
                        PositionComponent monsterPos = (PositionComponent) targetMonster.getComponent(GameConfig.COMPONENT_ID.POSITION);
                        PositionComponent towerPos = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);
                        try {
                            EntityFactory.getInstance().createBullet(tower.getTypeID(), towerPos, monsterPos, attackComponent.getEffects(), tower.getMode());
                        } catch (Exception e) {
                            e.printStackTrace();
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
        return Utils.euclidDistance(new Point(towerPos.getX(), towerPos.getY()), new Point(monsterPos.getX(), monsterPos.getY()));
    }

    public EntityECS findTargetMonsterByStrategy(int strategy, List<EntityECS> monsterInRange) {
        for (EntityECS monster : monsterInRange) {
            if (monster.getTypeID() == GameConfig.ENTITY_ID.DARK_GIANT) {
                return monster;
            }
        }
        // TODO: Implement when have burrowed monster
//        for (EntityECS monster: monsterInRange) {
//            UnderGround
//        }

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
                targetMonster = monsterInRange.get(maxHPIndex);
                break;
            }
            case GameConfig.TOWER_TARGET_STRATEGY.MIN_HP:
                break;
            case GameConfig.TOWER_TARGET_STRATEGY.MAX_DISTANCE:
                break;
            default:
                throw new Error("Invalid strategy");
        }
        return targetMonster;
    }
}
