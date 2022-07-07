package battle.Component.Component;

import battle.Common.Utils;
import battle.Component.EffectComponent.*;
import battle.Component.EffectComponent.EffectComponent;
import battle.Config.GameConfig;

import java.awt.*;
import java.util.ArrayList;

public class Component {
    public int typeID = 0;
    public String name = "ComponentECS";
    public int id;
    public boolean _active;

    public Component() {
    }

    public Component(int typeID) {
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







