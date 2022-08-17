package battle.system;

import battle.Battle;
import battle.common.UUIDGeneratorECS;
import battle.component.common.Component;
import battle.entity.EntityECS;

import java.util.HashMap;
import java.util.Map;

public abstract class SystemECS {
    private String name;
    private int typeId;
    long currentMillis;
    long pastMillis;
    double tick;
    private long id;
    private Map<Long, EntityECS> entityStore;

    public SystemECS(int typeId, String name,long systemId) {
        this.typeId = typeId;
        this.id = systemId;
        this.entityStore = new HashMap<>();
    }

    public int getTypeId() {
        return this.typeId;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public abstract void run(Battle battle) throws Exception;

    public double getElapseTime() {
        this.currentMillis = java.lang.System.currentTimeMillis();
        this.tick = (double) currentMillis - pastMillis;
        this.pastMillis = currentMillis;
        return this.tick;
    }

    public abstract boolean checkEntityCondition(EntityECS entity, Component component);

    public final void addEntity(EntityECS entity, Component component) {
        if (this.checkEntityCondition(entity, component)) {
            this.entityStore.put(entity.getId(), entity);
        }
    }

    public final void removeEntity(EntityECS entity, Component component) throws Exception {
        if (!this.checkEntityCondition(entity, component)) return;
        if (!this.entityStore.containsKey(entity.getId())) {
            throw new Exception("Entity id = " + entity.getId() + " doesn't exist - " + this.getName());
        }

        this.entityStore.remove(entity.getId());
    }
}
