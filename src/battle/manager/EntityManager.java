package battle.manager;


import battle.common.UUIDGeneratorECS;
import battle.component.common.Component;
import battle.entity.EntityECS;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class EntityManager extends ManagerECS {
    private Map<Long, EntityECS> entities;
    private final String name = "EntityManager";

    public EntityManager() {
        super();
        entities = new HashMap<>();
    }

    public void destroy(EntityECS entityECS) {
        EntityECS entity = entities.get(entityECS.getId());
        for (Map.Entry<Integer, Component> entry : entities.get(entity.getId()).getComponents().entrySet()) {
            entry.getValue().setActive(false);
        }
        entities.remove(entity.getId());
    }


    public void createEntity() {
//        throw new NotImplementedException();
    }

    public EntityECS getEntity(int entityID) {
        return entities.get(entityID);
    }

    public List<EntityECS> getEntitiesHasComponents(List<Integer> componentTypeIDS) {
        List<EntityECS> entityList = new ArrayList<>();

        for (Map.Entry<Long, EntityECS> entry : this.entities.entrySet()) {
            EntityECS entity = entry.getValue();
            if (entity.getActive() && entity.hasAllComponent(componentTypeIDS)) {
                entityList.add(entity);
            } else if (!entry.getValue().getActive()) {
                // remove entity
                // delete this.entities[id];
            }
        }
        return entityList;
    }

    public void addEntity(EntityECS entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        this.entities.put(entity.getId(), entity);
    }

    public void remove(EntityECS entity) {
        entity.setActive(false);
        this.entities.remove(entity.getId());
    }

    public void showEntity()
    {
        for (Map.Entry<Long, EntityECS> entry : this.entities.entrySet()) {
            System.out.println(entry.getKey());
            entry.getValue().showComponent();
        }
    }
}
