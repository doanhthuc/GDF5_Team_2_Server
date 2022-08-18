package battle.system;

import battle.Battle;
import battle.common.UUIDGeneratorECS;
import battle.component.common.Component;
import battle.config.GameConfig;
import battle.entity.EntityECS;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class SystemECS {
    private String name;
    private int typeId;
    double tick;
    private long id;
    private Map<Long, EntityECS> entityStore;

    public SystemECS(int typeId, String name,long systemId) {
        this.typeId = typeId;
        this.name = name;
        this.id = systemId;
        this.entityStore = new ConcurrentHashMap<>();
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

    public Map<Long, EntityECS> getEntityStore() {
        return this.entityStore;
    }

    public abstract void run(Battle battle) throws Exception;

    public double getElapseTime() {
        return GameConfig.BATTLE.TICK_RATE;
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

    public final void removeEntity(EntityECS entity) {
        this.entityStore.remove(entity.getId());
    }
}
