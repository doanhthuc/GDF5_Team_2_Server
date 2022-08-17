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

public class LifeSystem extends SystemECS {
    private static final String SYSTEM_NAME = "LifeSystem";

    public LifeSystem(long id) {
        super(GameConfig.SYSTEM_ID.LIFE, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) {
        List<Integer> lifeComponentIDs = new ArrayList<>();
        lifeComponentIDs.add(LifeComponent.typeID);
        List<EntityECS> lifeEntity = battle.getEntityManager().getEntitiesHasComponents(lifeComponentIDs);
        for (EntityECS entity : lifeEntity) {
            LifeComponent lifeComponent = (LifeComponent) entity.getComponent(LifeComponent.typeID);
            MonsterInfoComponent monsterInfoComponent = (MonsterInfoComponent) entity.getComponent(MonsterInfoComponent.typeID);
            if (lifeComponent.getHp() <= 0) {
                battle.addPlayerEnergy(monsterInfoComponent.getGainEnergy(), entity.getMode());
                battle.getEntityManager().remove(entity);
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == LifeComponent.typeID;
    }
}
