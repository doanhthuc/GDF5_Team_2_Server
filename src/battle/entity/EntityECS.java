package battle.entity;


import battle.common.EntityMode;
import battle.common.UUIDGeneratorECS;
import battle.common.Utils;
import battle.component.common.Component;
import battle.manager.ComponentManager;
import battle.manager.SystemManager;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityECS {
    private static final int MAX_ENTITY_TYPE = 100;
    private int typeID;
    private Map<Integer, Component> components;
    private long id;
    private boolean active;
    private EntityMode mode;
    private int[] bitmask;
    private ComponentManager componentManager;
    private SystemManager systemManager;

    public EntityECS(int typeID, EntityMode mode, long id, ComponentManager componentManager, SystemManager systemManager) {
        this.typeID = typeID;
        this.components = new ConcurrentHashMap<>();
        this.id = id;
        this.active = true;
        this.mode = mode;
        this.bitmask = new int[MAX_ENTITY_TYPE];
        this.componentManager = componentManager;
        this.systemManager = systemManager;
    }

    public EntityECS addComponent(Component component) throws Exception {
        if (this.components.get(component.getTypeID()) != null) {
            this.componentManager.remove(component);
        }
        component.setActive(true);
        this.components.put(component.getTypeID(), component);
        this.componentManager.add(component);
        this.bitmask[component.getTypeID()] = 1;
        this.systemManager.addEntityIntoSystem(this, component);
        return this;
    }

    public void removeComponent(Component cpn) {
        Component component = this.components.get(cpn.getTypeID());
        if (component != null) {
            this.systemManager.removeEntityFromSystem(this, cpn);
            this.componentManager.remove(component);
            this.components.remove(component.typeID);
            this.bitmask[component.getTypeID()] = 0;
        }
    }

    public Component getComponent(int typeID) {
        return this.components.get(typeID);
    }

    public boolean hasAllComponent(List<Integer> typeIDs) {
        int c = 0;
        for (Integer typeID : typeIDs) {
            if (this._hasComponent(typeID)) {
                c++;
            } else {
                return false;
            }
        }
        return (c == typeIDs.size());
    }

    public boolean _hasComponent(int typeID){
        return this.bitmask[typeID] == 1;
    }

    @Override
    public String toString() {
        return "EntityECS{" +
                "typeID=" + this.typeID +
                ", components=" + components +
                ", id=" + id +
                ", active=" + active +
                ", mode=" + mode +
                '}';
    }

    public int getTypeID() {
        return typeID;
    }

    public Map<Integer, Component> getComponents() {
        return this.components;
    }

    public long getId() {
        return id;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EntityMode getMode() {
        return mode;
    }

    public void setMode(EntityMode mode) {
        this.mode = mode;
    }

    public void showComponent() {
        Component component = null;
        for (int i = 1; i <= 100; i++) {
            component = this.getComponent(i);
            if (component != null) {
                System.out.print(component.getTypeID() + " ");
            }
        }
    }

    public void createSnapshot(ByteBuffer byteBuffer) {
        short activeShort = Utils.getInstance().convertBoolean2Short(active);
        short modeShort = Utils.getInstance().convertMode2Short(mode);
        byteBuffer.putInt(typeID);
        byteBuffer.putLong(id);
        byteBuffer.putShort(activeShort);
        byteBuffer.putShort(modeShort);
    }
}
