package battle.component.Component;

import battle.common.Utils;
import battle.config.GameConfig;

public class Component {
    public int typeID = 0;
    public String name = "ComponentECS";
    public int id;
    public boolean _active;

    public Component() {
    }

    public Component(int typeID) {
        this.name=GameConfig.COMPONENT_NAME.NAME.get(typeID);
      //  System.out.println(typeID+" "+this.name);
        this.typeID = typeID;
        this.id=Utils.UUID.genIncrementID();
        this._active = true;
    }

    public boolean getActive() {
        return this._active;
    }

    public void setActive(boolean active) {
        this._active = active;
    }
}







