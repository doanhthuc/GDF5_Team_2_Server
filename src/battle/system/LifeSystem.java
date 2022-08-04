package battle.system;

import battle.Battle;
import battle.component.info.LifeComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class LifeSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.LIFE;

    public LifeSystem() {
        super(GameConfig.SYSTEM_ID.LIFE);
        java.lang.System.out.println("new LifeSystem");
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
                battle.addPlayerEnergy(monsterInfoComponent.getEnergy(), entity.getMode());
                battle.getEntityManager().remove(entity);
            }
        }
    }
}
