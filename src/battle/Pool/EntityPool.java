package battle.Pool;

import battle.Entity.EntityECS;

import java.util.ArrayList;

public class EntityPool {
    int entityTypeAmount=100;
    EntityECS invisibleEntity;
    public ArrayList<ArrayList<EntityECS>> pool = new ArrayList<>();

    public void initiate() {
        for (int i = 0; i <= this.entityTypeAmount; i++) {
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
        if (this.pool.get(entity.typeID) != null) {
            this.pool.get(entity.typeID).add(entity);
        } else {
            this.pool.get(entity.typeID).add(entity);
        }
    }
}
