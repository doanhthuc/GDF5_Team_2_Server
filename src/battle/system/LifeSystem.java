package battle.system;

import battle.Battle;
import battle.component.common.Component;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LifeSystem extends SystemECS {
    private static final String SYSTEM_NAME = "LifeSystem";

    public LifeSystem(long id) {
        super(GameConfig.SYSTEM_ID.LIFE, SYSTEM_NAME,id);
    }

    @Override
    public void run(Battle battle) {
        for (Map.Entry<Long, EntityECS> mapElement : this.getEntityStore().entrySet()) {
            EntityECS monster = mapElement.getValue();
            if (!monster._hasComponent(LifeComponent.typeID)) continue;
            LifeComponent lifeComponent = (LifeComponent) monster.getComponent(LifeComponent.typeID);
            MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) monster.getComponent(MonsterInfoComponent.typeID);
            if (lifeComponent.getHp() <= 0) {
                battle.addPlayerEnergy(monsterInfoComponent.getGainEnergy(), monster.getMode());
                battle.getEntityManager().remove(monster);
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == LifeComponent.typeID;
    }
}
