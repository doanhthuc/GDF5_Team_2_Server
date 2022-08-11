package battle.config.conf.towerBuff;

import battle.config.conf.targetBuff.TargetBuffConfig;
import battle.config.conf.targetBuff.TargetBuffConfigItem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TowerBuffConfig {
    public static final short CELL_DAMAGE = 0;
    public static final short CELL_ATTACK_SPEED = 1;
    public static final short CELL_RANGE = 2;
    public static final short ATTACK_AURA = 3;
    public static final short ATTACK_SPEED_AURA = 4;
    public static final short ICE_MAN_ABILITY = 5;
    public static final short POTION_FROZEN = 6;
    public static final TowerBuffConfig INS = new TowerBuffConfig();
    private final Map<Short, TowerBuffConfigItem> buffMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(TowerBuffConfig.INS.buffMap);
    }

    private TowerBuffConfig() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("./conf/battle/json/TowerBuff.json");
            JsonObject obj = (JsonObject) parser.parse(reader);
            JsonObject buffObj = obj.getAsJsonObject("towerBuff");
            for (Map.Entry<String, JsonElement> entry : buffObj.entrySet()) {
                short key = Short.parseShort(entry.getKey());
                JsonObject value = (JsonObject) entry.getValue();
                TowerBuffConfigItem item = gson.fromJson(value.toString(), TowerBuffConfigItem.class);
                buffMap.put(key, item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TowerBuffConfigItem getTowerBuffConfig(short type) {
        return buffMap.get(type);
    }
}
