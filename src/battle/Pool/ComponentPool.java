package battle.Pool;

import battle.Component.Component;

import javax.swing.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ComponentPool {
    public String name = "ComponentObjectPool";
    Map<Integer, Map<Integer, Component>> unlocked = new Map<Integer, Map<Integer, Component>>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Map<Integer, Component> get(Object key) {
            return null;
        }

        @Override
        public Map<Integer, Component> put(Integer key, Map<Integer, Component> value) {
            return null;
        }

        @Override
        public Map<Integer, Component> remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends Map<Integer, Component>> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<Integer> keySet() {
            return null;
        }

        @Override
        public Collection<Map<Integer, Component>> values() {
            return null;
        }

        @Override
        public Set<Entry<Integer, Map<Integer, Component>>> entrySet() {
            return null;
        }
    };
    Map<Integer, Map<Integer, Component>> locked = new Map<Integer, Map<Integer, Component>>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Map<Integer, Component> get(Object key) {
            return null;
        }

        @Override
        public Map<Integer, Component> put(Integer key, Map<Integer, Component> value) {
            return null;
        }

        @Override
        public Map<Integer, Component> remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends Map<Integer, Component>> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<Integer> keySet() {
            return null;
        }

        @Override
        public Collection<Map<Integer, Component>> values() {
            return null;
        }

        @Override
        public Set<Entry<Integer, Map<Integer, Component>>> entrySet() {
            return null;
        }
    };

    public ComponentPool() {

    }
    boolean validate(Component component){
        return component.getActive()==false;
    }
    void checkOut(int typeID)
    {
        Map<Integer,Component> unlockedMap = this.unlocked.get(typeID);
        if (unlockedMap.size()>0){
            Component component = unlockedMap.values().iterator().next();
            if (this.validate(component)) {
                unlockedMap.remove(component.id);
                if (this.locked.get(typeID)!=null)
                {
                    
                }
            }
        }
    }
}
