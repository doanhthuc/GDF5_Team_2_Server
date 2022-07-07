package battle.Pool;

import battle.Component.Component.Component;

import java.util.HashMap;
import java.util.Map;

public class ComponentPool {
    public String name = "ComponentObjectPool";
    Map<Integer, Map<Integer, Component>> unlocked = new HashMap<>();
    Map<Integer, Map<Integer, Component>> locked = new HashMap<>();
    public ComponentPool() {

    }

    boolean validate(Component component) {
        return component.getActive() == false;
    }

    public Component checkOut(int typeID) {
        Map<Integer, Component> unlockedMap = this.unlocked.get(typeID);
        if (unlockedMap.size() > 0) {
            Component component = unlockedMap.values().iterator().next();
            if (this.validate(component)) {
                unlockedMap.remove(component.id);
                if (this.locked.get(typeID) != null) {
                    this.locked.put(typeID, new HashMap<>());
                    component.setActive(true);
                    return component;
                }
            } else {
                unlockedMap.remove(component.typeID);
            }
        }
        return null;
    }

    public void checkIn(Component component) {
        if ((this.locked.get(component.typeID) != null)
                && (this.locked.get(component.typeID).get(component.id) != null)){
            this.locked.get(component.typeID).remove(component.id);
        }

        if (this.unlocked.get(component.typeID)==null) {
            this.unlocked.put(component.typeID, new HashMap<>());
        }
        this.unlocked.get(component.typeID).put(component.id, component);
    }
}
