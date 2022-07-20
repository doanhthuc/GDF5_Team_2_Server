package battle.system;

import battle.component.InfoComponent.LifeComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class LifeSystem extends SystemECS{
    public int id = GameConfig.SYSTEM_ID.LIFE;
    public LifeSystem(){
        super();
        java.lang.System.out.println("new LifeSystem");
    }

    @Override
    public void run(){
        List<Integer> lifeComponentIDs= new ArrayList<>();
        lifeComponentIDs.add(GameConfig.COMPONENT_ID.LIFE);
        List<EntityECS> lifeEntity= EntityManager.getInstance().getEntitiesHasComponents(lifeComponentIDs);
        for(EntityECS entity: lifeEntity)
        {
            LifeComponent lifeComponent= (LifeComponent) entity.getComponent(GameConfig.COMPONENT_ID.LIFE);
            if (lifeComponent.getHp()<=0) EntityManager.getInstance().destroyEntity(entity.getId());
        }
    }
}
