package battle.System;

import battle.Common.Point;
import battle.Common.Utils;
import battle.Component.Component.AttackComponent;
import battle.Component.Component.PositionComponent;
import battle.Component.InfoComponent.MonsterInfoComponent;
import battle.Config.GameConfig;
import battle.Entity.EntityECS;
import battle.Factory.EntityFactory;
import battle.Manager.EntityManager;
import bitzero.core.P;

import java.util.ArrayList;

public class AttackSystem extends System {
    public int id = GameConfig.SYSTEM_ID.ATTACK;
    public String name = "AttackSystem";

    public AttackSystem() {
        java.lang.System.out.println("NOT OPENING");
    }
    @Override
    public void run() {
        this.currentMillis = java.lang.System.currentTimeMillis();
        this.tick = currentMillis - pastMillis;
        this.pastMillis = currentMillis;
        ArrayList<Integer> typeIDs = new ArrayList<>();
        typeIDs.add(GameConfig.COMPONENT_ID.ATTACK);
        ArrayList<EntityECS> towerList = EntityManager.getInstance().getEntitiesHasComponents(typeIDs);

        while (typeIDs.size() > 0) {
            typeIDs.remove(0);
        }
        typeIDs.add(GameConfig.COMPONENT_ID.MONSTER_INFO);
        ArrayList<EntityECS> monsterList = EntityManager.getInstance().getEntitiesHasComponents(typeIDs);

        for (EntityECS tower : towerList) {
            AttackComponent attackComponent = (AttackComponent) tower.getComponent(GameConfig.COMPONENT_ID.ATTACK);
            if (attackComponent.countdown > 0) {
                attackComponent.countdown -= tick;
            }
            if (attackComponent.countdown <= 0) {
                ArrayList<EntityECS> monsterInRange = new ArrayList<>();
                for (EntityECS monster : monsterList) {
                    double distance = this._distanceFrom(tower, monster);
                    if (distance <= attackComponent.range) monsterInRange.add(monster);
                }
                if (monsterInRange.size()>0){
                    EntityECS targetMonster= this._findtargetMonsterByStratgy(attackComponent.targetStategy,monsterInRange);
                    PositionComponent monsterPos = (PositionComponent) targetMonster.getComponent(GameConfig.COMPONENT_ID.POSITION);
                    PositionComponent towerPos = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);
                    EntityFactory.getInstance().createBullet(tower.typeID,towerPos.getPos(),monsterPos.getPos(),attackComponent.effects);
                    attackComponent.countdown = attackComponent.speed;
                }
            }

        }
    }

    public double _distanceFrom(EntityECS tower, EntityECS monster) {
        PositionComponent towerPos = (PositionComponent) tower.getComponent(GameConfig.COMPONENT_ID.POSITION);
        PositionComponent monsterPos = (PositionComponent) monster.getComponent(GameConfig.COMPONENT_ID.POSITION);
        return Utils.euclidDistance(new Point(towerPos.x, towerPos.y), new Point(monsterPos.x, monsterPos.y));
    }
    public EntityECS _findtargetMonsterByStratgy(int stategy,ArrayList<EntityECS> monsterInRange)
    {
        return monsterInRange.get(0);
    }
}
