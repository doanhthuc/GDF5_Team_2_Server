package battle.manager;


import battle.Battle;
import battle.common.UUIDGeneratorECS;
import battle.component.common.Component;
import battle.entity.EntityECS;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager extends ManagerECS {
    private Map<Long, EntityECS> entities;
    private final String name = "EntityManager";
    private Battle battle;

    public EntityManager(Battle battle) {
        super();
        entities = new ConcurrentHashMap<>();
        this.battle = battle;
    }

    public void destroy(EntityECS entityECS) {
        if (!entities.containsKey(entityECS.getId())) return;
        for (Map.Entry<Integer, Component> entry : entities.get(entityECS.getId()).getComponents().entrySet()) {
            Component component = entry.getValue();
            battle.getComponentManager().remove(component);
            entityECS.removeComponent(component);
        }
        this.remove(entityECS);
    }


    public void createEntity() {
//        throw new NotImplementedException();
    }

    public EntityECS getEntity(long entityID) {
        return entities.get(entityID);
    }

    public List<EntityECS> getEntitiesHasComponents(List<Integer> componentTypeIDS) {
        List<EntityECS> entityList = new ArrayList<>();
        if (this.entities.size() > 0) {
            for (Map.Entry<Long, EntityECS> entry : this.entities.entrySet()) {
                EntityECS entity = entry.getValue();
                if (entity.getActive() && entity.hasAllComponent(componentTypeIDS)) {
                    entityList.add(entity);
                } else if (!entry.getValue().getActive()) {
                    // remove entity
                    // delete this.entities[id];
                }
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

    public void showEntity() {
        for (Map.Entry<Long, EntityECS> entry : this.entities.entrySet()) {
            System.out.println(entry.getKey());
            entry.getValue().showComponent();
        }
    }
}
