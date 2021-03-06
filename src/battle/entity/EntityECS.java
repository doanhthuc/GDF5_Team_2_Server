package battle.entity;


import battle.common.EntityMode;
import battle.common.UUIDGeneratorECS;
import battle.component.common.Component;
import battle.manager.ComponentManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityECS {
    private int typeID;

    private Map<Integer, Component> components;
    private long id;
    private boolean active;
    private EntityMode mode;
    public EntityECS(int typeID, EntityMode mode) {
        this.typeID = typeID;
        this.components = new HashMap<>();
        this.id = UUIDGeneratorECS.genEntityID();
        this.active = true;
        this.mode = mode;
    }

    public EntityECS addComponent(Component component) {
        if (this.components.get(component.getTypeID()) != null) {
            //TODO: check override or not
        }
        component.setActive(true);
        this.components.put(component.getTypeID(), component);
        return this;
    }

    public void removeComponent(Component cpn, ComponentManager componentManager) {
        Component component = this.components.get(cpn.getTypeID());
        if (component != null) {
            componentManager.remove(component);
            this.components.remove(component.typeID);
        }
    }

    public Component getComponent(int typeID) {
        return this.components.get(typeID);
    }

    public boolean hasAllComponent(List<Integer> typeIDs) {
        int c = 0;
        for (Integer typeID : typeIDs) {
            if (this.getComponent(typeID) != null) {
                c++;
            }
        }
        return (c == typeIDs.size());
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
        return components;
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
                System.out.print(component.getTypeID()+" ");
            }
        }
    }
}
