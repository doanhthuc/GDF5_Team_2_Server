package battle.config.conf.potion;


import battle.config.conf.monster.MultiplierItem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PotionConfig {
    public static final short FIREBALL = 0;
    public static final short FROZEN = 1;
    public static final short TRAP = 2;
    public static final PotionConfig INS = new PotionConfig();
    private final Map<Short, PotionConfigItem> potionMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(PotionConfig.INS.getPotionConfig(PotionConfig.FIREBALL));
    }

    private PotionConfig() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("./conf/battle/json/Potion.json");
            JsonObject obj = (JsonObject) parser.parse(reader);

            JsonObject potionObj = obj.getAsJsonObject("potion");
            for (Map.Entry<String, JsonElement> entry : potionObj.entrySet()){
                short key = Short.parseShort(entry.getKey());
                JsonObject value = (JsonObject) entry.getValue();
                PotionConfigItem item = gson.fromJson(value.toString(), PotionConfigItem.class);
                potionMap.put(key,item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PotionConfigItem getPotionConfig(short type) {
        return potionMap.get(type);
    }
}
