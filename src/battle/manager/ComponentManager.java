package battle.manager;


import battle.component.common.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentManager extends ManagerECS {
    private static final String name = "ComponentManager";
    private final Map<Long, Component> storeInstance;
    private final Map<Integer, Class> storeCls;

    public ComponentManager() {
        super();
        this.storeCls = new ConcurrentHashMap<>();
        this.storeInstance = new ConcurrentHashMap<>();
    }


    public void add(Component component) throws Exception {
        if (this.storeInstance.get(component.getId()) != null) {
            System.out.println(this.storeInstance.toString());
            throw new Exception("Component with typeID = " + component.getTypeID() + ", id = " + component.getId() + " exists.");
        }
        this.storeInstance.put(component.getId(), component);
    }

    public Component findByInstanceID(long instanceID) {
        return this.storeInstance.get(instanceID);
    }

    public void remove(Component component) {
        component.setActive(false);
        this.storeInstance.remove(component.getId());
    }

    public String getName() {
        return name;
    }
}
