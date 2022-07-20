package battle.Pool;

import battle.Component.Component.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentPool {
    public String name = "ComponentObjectPool";
    ArrayList<ArrayList<Component>> _store= new ArrayList<>();

    public ComponentPool() {
        for(int i=0;i<=100;i++)
            this._store.add(new ArrayList<>());
    }

    boolean validate(Component component) {
        return component.getActive() == false;
    }

    public Component checkOut(int typeID) {
       for(Component component: this._store.get(typeID))
       {
           if (component.getActive()==false) {
               component.setActive(true);
               return component;
           }
       }
       return null;
    }

    public void checkIn(Component component) {

    }
}
