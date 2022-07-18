package battle.manager;


import battle.component.Component.Component;
import battle.entity.EntityECS;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager extends ManagerECS {
    private static Map<Long, EntityECS> entities = null;
    private static EntityManager instance = null;
    private final String name = "EntityManager";

    public EntityManager() {
        super();
        entities = new HashMap<>();
    }

    public static void destroy(EntityECS entityECS) {
        EntityECS entity = entities.get(entityECS.getId());
        for (Map.Entry<Integer, Component> entry : entities.get(entity.getId()).getComponents().entrySet()) {
            entry.getValue().setActive(false);
        }
        entities.remove(entity.getId());
    }

    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    public void createEntity() {
        throw new NotImplementedException();
    }

    public EntityECS getEntity(int entityID) {
        return this.entities.get(entityID);
    }

    public List<EntityECS> getEntitiesHasComponents(List<Integer> componentTypeIDS) {
        List<EntityECS> entityList = new ArrayList<>();

        for (Map.Entry<Long, EntityECS> entry : entities.entrySet()) {
            EntityECS entity = entry.getValue();
            if (entity.isActive() && entity.hasAllComponent(componentTypeIDS)) {
                entityList.add(entity);
            } else if (!entry.getValue().isActive()) {
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
        entities.put(entity.getId(), entity);
    }

    public void remove(EntityECS entity) {
        entity.setActive(false);
        entities.remove(entity.getId());
    }
}
