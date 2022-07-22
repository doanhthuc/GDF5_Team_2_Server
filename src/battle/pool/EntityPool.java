package battle.pool;

import battle.entity.EntityECS;

import java.util.ArrayList;
import java.util.List;

public class EntityPool {
    public ArrayList<ArrayList<EntityECS>> pool = new ArrayList<>();
    int entityTypeAmount = 100;
    EntityECS invisibleEntity;

    public EntityPool() {
        this.initiate();
    }

    public void initiate() {
        for (int i = 0; i < this.entityTypeAmount; i++) {
            this.pool.add(new ArrayList<EntityECS>());
        }
    }

    public EntityECS getInactiveEntity(int entityTypeID) {
        if (this.pool.get(entityTypeID) != null) {
            for (int i = 0; i < this.pool.get(entityTypeID).size(); i++) {
                invisibleEntity = this.pool.get(entityTypeID).get(i);
                invisibleEntity.setActive(true);
                break;
            }
        }
        return invisibleEntity;
    }

    public void push(EntityECS entity) {
        if (this.pool.get(entity.getTypeID()) != null) {
            this.pool.get(entity.getTypeID()).add(entity);
        } else {
            this.pool.get(entity.getTypeID()).add(entity);
        }
    }
}
