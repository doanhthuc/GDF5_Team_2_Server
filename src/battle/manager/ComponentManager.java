package battle.manager;


import battle.component.Component.Component;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {
    public Map<Integer, Component> _storeInstance;
    public Map<Integer, Component> _storeCls;
    String name = "ComponentManager";
    private static ComponentManager _instance = null;

    public ComponentManager() {
        this._storeCls = new HashMap();
        this._storeInstance = new HashMap<>();
    }

    public void registerClass(Component cls) {
        this._storeInstance.put(cls.typeID, cls);
    }

    public void getClass(int typeID) {
        if (this._storeCls.get(typeID) == null) {

        }
        return;
    }

    public void add(Component component) {
        if (this._storeInstance.get(component.id) == null) {

        }
        this._storeInstance.put(component.id, component);
    }

    public Component findByInstanceID(int instanceID) {
        return this._storeInstance.get(instanceID);
    }

    public void remove(Component component) {
        this._storeInstance.remove(component.id);
    }

    public static ComponentManager getInstance() {
        if (_instance == null) _instance = new ComponentManager();
        return _instance;
    }
}
