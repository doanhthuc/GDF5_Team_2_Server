package battle.component.common;

import battle.common.UUIDGeneratorECS;

public class Component {
    public  int typeID = 0;
    private String name = "ComponentECS";
    private long id;
    private boolean active;

    public Component() {
        this.id = UUIDGeneratorECS.genComponentID();
        this.active = true;
    }

    public Component(int typeID) {
        this.typeID = typeID;
        this.name = "ComponentECS";
        this.id = UUIDGeneratorECS.genComponentID();
        this.active = true;
    }

    public int getTypeID() {
        return this.typeID;
    }

    public String getName() {
        return name;
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
}







