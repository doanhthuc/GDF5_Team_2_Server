package battle.Entity;

import battle.Common.Utils;
import battle.Component.Component.Component;
import battle.Config.GameConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntityECS {
    public int typeID;
    public Map<Integer, Component> components;
    public int id;
    public boolean _active;
    public String mode;

    public EntityECS(int typeID) {
        this.typeID = typeID;
        this.components = new HashMap<>();
        this.id = Utils.UUID.genIncrementID();
        this._active = true;
    }

    public EntityECS(int typeID, String mode) {
        this.typeID = typeID;
        this.components = new HashMap<>();
        this.id = Utils.UUID.genIncrementID();
        this._active = true;
        if (mode == "") mode = GameConfig.PLAYER;
        this.mode = mode;
    }

    public void addComponent(Component component) {
        if (this.components.get(component.typeID) != null) {
            //TODO: check override or not
        }
        this.components.put(component.typeID, component);
    }

    public void removeComponent(Component component) {
        this.components.remove(component.typeID);
    }

    public Component getComponent(int typeID) {
        return this.components.get(typeID);
    }

    public boolean hasAllComponent(ArrayList<Integer> typeIDs) {
        int c = 0;
        for (Integer typeID : typeIDs) {
           // System.out.println("AllTypeIDS"+ typeID);
            if (this.getComponent(typeID) != null) {
                c++;
            }
        }
//        System.out.println(c);
//        System.out.println();
        return (c == typeIDs.size());
    }

    public void setActive(boolean value) {
        this._active = value;
    }

    public boolean getActive() {
        return this._active;
    }
}
