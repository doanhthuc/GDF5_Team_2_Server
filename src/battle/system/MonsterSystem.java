package battle.system;

import battle.Battle;
import battle.common.Point;
import battle.common.Utils;
import battle.component.common.Component;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MonsterSystem extends SystemECS {
    private static final String SYSTEM_NAME = "MonsterSystem";

    public MonsterSystem(long id) {
        super(GameConfig.SYSTEM_ID.MONSTER, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) {
        //Get MonsterList
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS monster = mapElement.getValue();
            if (!monster._hasComponent(PositionComponent.typeID)) continue;

            PositionComponent monsterPos = (PositionComponent) monster.getComponent(PositionComponent.typeID);
            Point posTile = Utils.pixel2Tile(monsterPos.getX(), monsterPos.getY(), monster.getMode());
            if (posTile.x == GameConfig.HOUSE_POSITION.x && posTile.y == GameConfig.HOUSE_POSITION.y) {
                MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);
                battle.minusPlayerHP(monsterInfoComponent.getEnergy(), monster.getMode());
                battle.addPlayerEnergy(monsterInfoComponent.getEnergy() * 10, monster.getMode());
                battle.getEntityManager().destroy(monster);
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == MonsterInfoComponent.typeID;
    }
}
