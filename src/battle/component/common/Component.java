package battle.component.common;

import battle.common.UUIDGeneratorECS;
import battle.common.Utils;

import java.nio.ByteBuffer;

public class Component {
    public int typeID = 0;
    private String name = "ComponentECS";
    private long id;
    private boolean active;

    public Component() {
        this.active = true;
    }

    public Component(int typeID) {
        this.typeID = typeID;
        this.name = "ComponentECS";
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

    public void setId(long id) {
        this.id = id;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Component{" +
                "typeID=" + typeID +
                ", name='" + this.getName() + '\'' +
                ", id=" + id +
                ", active=" + active +
                '}';
    }

    public void createData(ByteBuffer bf) {
        short activeShort = Utils.convertBoolean2Short(active);
        bf.putInt(typeID);
        bf.putLong(id);
        bf.putShort(activeShort);
    }
}







