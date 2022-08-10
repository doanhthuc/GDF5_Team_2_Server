package battle.config.conf.targetBuff;

import battle.config.conf.monster.MonsterConfig;
import battle.config.conf.monster.MonsterConfigItem;
import battle.config.conf.tower.TowerConfigItem;
import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TargetBuffConfig {
    public static final short BULLET_OIL_GUN = 0;
    public static final short BULLET_ICE_GUN = 1;
    public static final short POTION_FROZEN = 2;
    public static final short POTION_HEAL = 3;
    public static final short POTION_SPEED_UP = 4;
    public static final short DESERT_KING_ABILITY = 5;
    public static final short NINJA_ABILITY = 6;
    public static final short SATYR_ABILITY = 7;
    public static final TargetBuffConfig INS = new TargetBuffConfig();
    private final Map<Short, TargetBuffConfigItem> buffMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(TargetBuffConfig.INS.buffMap);
    }

    private TargetBuffConfig() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("./src/battle/config/conf/json/TargetBuff.json");
            JsonObject obj = (JsonObject) parser.parse(reader);
            JsonObject buffObj = obj.getAsJsonObject("targetBuff");
            for (Map.Entry<String, JsonElement> entry : buffObj.entrySet()) {
                short key = Short.parseShort(entry.getKey());
                JsonObject value = (JsonObject) entry.getValue();
                TargetBuffConfigItem item = gson.fromJson(value.toString(), TargetBuffConfigItem.class);
                buffMap.put(key, item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TargetBuffConfigItem getTargetBuffConfig(short type) {
        return buffMap.get(type);
    }
}
