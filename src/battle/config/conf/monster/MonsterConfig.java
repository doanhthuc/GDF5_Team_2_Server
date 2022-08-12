package battle.config.conf.monster;


import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MonsterConfig {


    public static final short SWORDSMAN = 0;
    public static final short ASSASSIN = 1;
    public static final short GIANT = 2;
    public static final short BAT = 3;
    public static final short NINJA = 4;
    public static final short DARK_GIANT = 5;
    public static final short SATYR = 6;
    public static final short DESERT_KING = 7;
    public static final short ICEMAN = 8;
    public static final short GOLEM = 9;
    public static final short GOLEM_MINION = 10;
    public static final short DEMON_TREE = 11;
    public static final short DEMON_TREE_MINION = 12;

    public static final MonsterConfig INS = new MonsterConfig();
    private final Map<Short,MultiplierItem> mulMap = new HashMap<>();
    private final Map<Short,MonsterConfigItem> monsterMap = new HashMap<>();

    private MonsterConfig() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("./conf/battle/json/Monster.json");
            JsonObject obj = (JsonObject) parser.parse(reader);

            JsonObject mulObj = obj.getAsJsonObject("multiplier");
            for (Map.Entry<String, JsonElement> entry : mulObj.entrySet()){
                short key = Short.parseShort(entry.getKey());
                JsonObject value = (JsonObject) entry.getValue();
                MultiplierItem item = gson.fromJson(value.toString(), MultiplierItem.class);
                mulMap.put(key,item);
            }

            JsonObject monsterObj = obj.getAsJsonObject("monster");
            for (Map.Entry<String, JsonElement> entry : monsterObj.entrySet()) {
                short key = Short.parseShort(entry.getKey());
                JsonObject value = (JsonObject) entry.getValue();
                MonsterConfigItem item = gson.fromJson(value.toString(), MonsterConfigItem.class);
                monsterMap.put(key, item);
            }

        } catch (Exception e){
            Debug.trace(e);
        }
    }
    public int getMultiplierSize() {
        return mulMap.size();
    }

    public MultiplierItem getMultiplierItem(short id){
        return  mulMap.get(id);
    }
    public MonsterConfigItem getMonsterConfig(short type){
        return  monsterMap.get(type);
    }

}
