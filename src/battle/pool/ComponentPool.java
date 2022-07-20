package battle.pool;

import battle.component.common.Component;

import java.util.ArrayList;
import java.util.HashMap;

public class ComponentPool {
    public String name = "ComponentObjectPool";
    private HashMap<Integer, ArrayList<Component>> store;

    public ComponentPool() {
        store = new HashMap<>();
    }

    boolean validate(Component component) {
        return !component.getActive();
    }

    public Component checkOut(int typeID) {
        for (Component component : this.store.get(typeID)) {
            if (!component.getActive()) {
                component.setActive(true);
                return component;
            }
        }
        return null;
    }

    public void checkIn(Component component) {
        if (store.containsKey(component.getTypeID())) {
            store.get(component.getTypeID()).add(component);
        } else {
            ArrayList<Component> list = new ArrayList<>();
            list.add(component);
            store.put(component.getTypeID(), list);
        }
    }
}
