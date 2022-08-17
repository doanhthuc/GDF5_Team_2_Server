package battle.manager;

import battle.component.common.Component;
import battle.entity.EntityECS;
import battle.system.SystemECS;

public class SystemManager extends ManagerECS{
    private final int MAX_SYSTEM = 100;
    private SystemECS storeInstance[];

    public SystemManager() {
        this.storeInstance = new SystemECS[MAX_SYSTEM];
    }

    public void add(SystemECS system) throws Exception {
        if (this.storeInstance[system.getTypeId()] == null) {
            throw new Exception("System name = " + system.getName() + ", id = " + system.getId() + " exists");
        }
        this.storeInstance[system.getTypeId()] = system;
    }

    public void remove(SystemECS systemECS) {
        this.storeInstance[systemECS.getTypeId()] = null;
    }

    public void addEntityIntoSystem(EntityECS entity, Component component) {
        for (SystemECS systemECS : this.storeInstance) {
            systemECS.addEntity(entity, component);
        }
    }

    public void removeEntityFromSystem(EntityECS entity, Component component) {
        for (SystemECS systemECS : this.storeInstance) {
            try {
                systemECS.removeEntity(entity, component);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
