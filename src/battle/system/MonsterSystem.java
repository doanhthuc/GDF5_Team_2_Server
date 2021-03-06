package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.PositionComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class MonsterSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.MONSTER;

    public MonsterSystem() {
        super(GameConfig.SYSTEM_ID.MONSTER);
        java.lang.System.out.println("new MonsterSystem");
    }

    @Override
    public void run(Battle battle) {
        //Get MonsterList
        List <Integer> monsterInfoIds = new ArrayList<>();
        monsterInfoIds.add(GameConfig.COMPONENT_ID.MONSTER_INFO);
        List <EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(monsterInfoIds);

        for(EntityECS monster : monsterList){
            PositionComponent monsterPos =(PositionComponent) monster.getComponent(PositionComponent.typeID);
            Point posTile = Utils.pixel2Tile(monsterPos.getX(),monsterPos.getY(),monster.getMode());
            if (posTile.x == GameConfig.HOUSE_POSITION.x && posTile.y == GameConfig.HOUSE_POSITION.y)
            {
                MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);
                // TODO: Minus House Energy and Add Energy for player
                battle.getEntityManager().destroy(monster);
            }
        }
    }
}
