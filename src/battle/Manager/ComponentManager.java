package battle.Manager;


import battle.Component.Component.Component;
import org.apache.commons.lang.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager extends ManagerECS {
    private static final String name = "ComponentManager";
    private static ComponentManager instance = null;
    private final Map<Long, Component> storeInstance;
    private final Map<Integer, Class> storeCls;

    public ComponentManager() {
        super();
        this.storeCls = new HashMap<>();
        this.storeInstance = new HashMap<>();
    }

    public static ComponentManager getInstance() {
        if (instance == null) {
            instance = new ComponentManager();
        }
        return instance;
    }

    public void registerClass(Component cpn) {
        // TODO: implement here
        throw new NotImplementedException();
    }

    public Class getClass(int typeID) {
        // TODO: implement here
        throw new NotImplementedException();
    }

    public void add(Component component) throws Exception {
        if (this.storeInstance.get(component.getId()) == null) {
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
