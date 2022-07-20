package battle.System;

import battle.Component.InfoComponent.LifeComponent;
import battle.Config.GameConfig;
import battle.Entity.EntityECS;
import battle.Manager.EntityManager;

import java.util.ArrayList;

public class LifeSystem extends System{
    public int id = GameConfig.SYSTEM_ID.LIFE;
    public LifeSystem(){
        java.lang.System.out.println("new LifeSystem");
    }

    @Override
    public void run(){
        ArrayList<Integer> lifeComponentIDs= new ArrayList<>();
        lifeComponentIDs.add(GameConfig.COMPONENT_ID.LIFE);
        ArrayList<EntityECS> lifeEntity= EntityManager.getInstance().getEntitiesHasComponents(lifeComponentIDs);
        for(EntityECS entity: lifeEntity)
        {
            LifeComponent lifeComponent= (LifeComponent) entity.getComponent(GameConfig.COMPONENT_ID.LIFE);
            if (lifeComponent.hp<=0) EntityManager.getInstance().destroyEntity(entity.id);
        }
    }
}
