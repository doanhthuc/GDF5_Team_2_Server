package battle.config.conf.tower;

import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class TowerConfig {
    public static final short OWL = 0;
    public static final short WIZARD = 1;
    public static final short FROG = 2;
    public static final short BUNNY = 3;
    public static final short POLAR_BEAR = 4;
    public static final short GOAT = 5;
    public static final short SNAKE = 6;

    public static final TowerConfig INS = new TowerConfig();
    private int buildingTime = 1000;
    private final Map<Short, TowerConfigItem> towerMap = new HashMap<>();

    public static void main(String[] args) {
        TowerConfigItem item = TowerConfig.INS.getTowerConfig(TowerConfig.OWL);
        System.out.println(item);
    }

    public TowerConfig() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("./conf/battle/json/Tower.json");
            JsonObject obj = (JsonObject) parser.parse(reader);
            this.buildingTime = obj.get("buildingTime").getAsInt();

            JsonObject towerObj = obj.getAsJsonObject("tower");
            for (Map.Entry<String, JsonElement> entry : towerObj.entrySet()) {
                short key = Short.parseShort(entry.getKey());
                JsonObject value = (JsonObject) entry.getValue();
                TowerConfigItem item = gson.fromJson(value.toString(), TowerConfigItem.class);
                towerMap.put(key, item);
            }
        } catch (Exception e) {
            Debug.trace(e);
        }
    }

    public int getBuildingTime() {
        return this.buildingTime;
    }
    public TowerConfigItem getTowerConfig(short type){
        return towerMap.get(type);
    }

}
