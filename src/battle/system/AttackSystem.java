package battle.system;

import battle.common.Point;
import battle.common.Utils;
import battle.component.Component.AttackComponent;
import battle.component.Component.PositionComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;

import java.util.ArrayList;

public class AttackSystem extends System implements Runnable {
    public int id = GameConfig.SYSTEM_ID.ATTACK;
    public String name = "AttackSystem";

    public AttackSystem() {
//        java.lang.System.out.println("AttackSystem");
    }

    @Override
    public void run() {
        this.tick = this.getEclapseTime();
//        java.lang.System.out.println(this.tick);
        //Create List of Component TypeIDs
        ArrayList<Integer> typeIDTower = new ArrayList<>();
        typeIDTower.add(GameConfig.COMPONENT_ID.ATTACK);
        ArrayList<EntityECS> towerList = EntityManager.getInstance().getEntitiesHasComponents(typeIDTower);
//        for(EntityECS i:towerList) {
//            i.showComponent();
//        }

        ArrayList<Integer> typeIDMonster = new ArrayList<>();
        typeIDMonster.add(GameConfig.COMPONENT_ID.MONSTER_INFO);
        ArrayList<EntityECS> monsterList = EntityManager.getInstance().getEntitiesHasComponents(typeIDMonster);
//        for(EntityECS i:monsterList) {
//            i.showComponent();
//        }
        ArrayList<Integer> typeIDBullet = new ArrayList<>();
        typeIDBullet.add(GameConfig.COMPONENT_ID.BULLET_INFO);
        ArrayList<EntityECS> bulletList = EntityManager.getInstance().getEntitiesHasComponents(typeIDBullet);
//        for (EntityECS bullet : bulletList) {
//            bullet.showComponent();
//        }
        for (EntityECS tower : towerList) {
            AttackComponent attackComponent = (AttackComponent) tower.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            if (attackComponent.countdown > 0) {
                attackComponent.countdown -= tick;
            }
            if (attackComponent.countdown <= 0) {
                ArrayList<EntityECS> monsterInRange = new ArrayList<>();
                for (EntityECS monster : monsterList) {
                    double distance = this._distanceFrom(tower, monster);
                    //java.lang.System.out.println(distance+" "+attackComponent.range);
                    if (distance <= attackComponent.range) monsterInRange.add(monster);
                }
                if (monsterInRange.size() > 0) {
                    EntityECS targetMonster = this._findTargetMonsterByStrategy(attackComponent.targetStategy, monsterInRange);
                    //targetMonster.showComponent();
                    PositionComponent monsterPos = (PositionComponent) targetMonster.getComponent(GameConfig.COMPONENT_ID.POSITION);
                    PositionComponent towerPos = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);
                    EntityFactory.getInstance().createBullet(tower.typeID, towerPos.getPos(), monsterPos.getPos(), attackComponent.effects);
                    attackComponent.countdown = attackComponent.speed * 1000;
                }
            }

        }

    }

    public double _distanceFrom(EntityECS tower, EntityECS monster) {
        PositionComponent towerPos = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);
        PositionComponent monsterPos = (PositionComponent) monster.getComponent(GameConfig.COMPONENT_ID.POSITION);
        return Utils.euclidDistance(new Point(towerPos.x, towerPos.y), new Point(monsterPos.x, monsterPos.y));
    }

    public EntityECS _findTargetMonsterByStrategy(int strategy, ArrayList<EntityECS> monsterInRange) {
        return monsterInRange.get(0);
    }
}
