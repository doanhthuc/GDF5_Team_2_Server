package battle.system;

import battle.Battle;
import battle.common.EntityMode;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.PositionComponent;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Integer> monsterInfoIds = Arrays.asList(PositionComponent.typeID, MonsterInfoComponent.typeID);
        List<EntityECS> monsterList = battle.getEntityManager().getEntitiesHasComponents(monsterInfoIds);

        for (EntityECS monster : monsterList) {
            PositionComponent monsterPos = (PositionComponent) monster.getComponent(PositionComponent.typeID);
            Point posTile = Utils.pixel2Tile(monsterPos.getX(), monsterPos.getY(), monster.getMode());
            if (posTile.x == GameConfig.HOUSE_POSITION.x && posTile.y == GameConfig.HOUSE_POSITION.y) {
                MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);
                battle.minusPlayerHP(monsterInfoComponent.getEnergy(), monster.getMode());
                battle.addPlayerEnergy(monsterInfoComponent.getGainEnergy(), monster.getMode());
                battle.getEntityManager().destroy(monster);
            }
        }
    }
}
