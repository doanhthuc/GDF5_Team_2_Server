package battle.manager;


import battle.component.Component.Component;
import battle.entity.EntityECS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntityManager {
    String name = "EntityManager";
    Map<Integer, EntityECS> entities;
    private static EntityManager _instance = null;

    public EntityManager() {
        this.entities = new HashMap<>();
    }

    public void createEntity() {
        return;
    }

    public EntityECS getEntity(int entityID) {
        return this.entities.get(entityID);
    }

    public ArrayList<EntityECS> getEntitiesHasComponents(ArrayList<Integer> componentTypeIDS) {
        ArrayList<EntityECS> entityList = new ArrayList<>();
        //for(Integer i:componentTypeIDS) System.out.println("AllTypeIDS: "+i+" ");
        //System.out.println();
        for (Map.Entry<Integer, EntityECS> entry : entities.entrySet()) {
            EntityECS entity = entry.getValue();
            if (entity.getActive() && entity.hasAllComponent(componentTypeIDS)) {
                entityList.add(entity);
            }
        }
        return entityList;
    }

    public void addEntity(EntityECS entity) {
        this.entities.put(entity.id, entity);
    }

    public void destroyEntity(int id) {
        this.entities.get(id).setActive(false);
        for (Map.Entry<Integer, Component> entry : this.entities.get(id).components.entrySet()) {
            entry.getValue().setActive(false);
        }
        this.entities.remove(id);
    }

    public static EntityManager getInstance() {
        if (_instance == null) {
            _instance = new EntityManager();
        }
        return _instance;
    }
}
