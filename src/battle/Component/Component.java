package battle.Component;

import battle.Common.Utils;

public class Component {
    int typeID=0;
    String name= "ComponentECS";
    int id;
    boolean _active;
    public Component(int typeID , int currentID){
        this.typeID=typeID;
        this.id= currentID;
        this._active=true;
    }
    public boolean getActive(){
        return this._active;
    }

    public void setActive(boolean active){
        this._active=active;
    }
}
