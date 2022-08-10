package battle.config;

import battle.common.MonsterWaveScript;
import battle.common.MonsterWaveSlot;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonsterWaveConfig {
    public static final HashMap<Integer, MonsterWaveScript> monsterWaveScriptHashMap = new HashMap<>();
    public static final HashMap<Integer, Integer> monsterBaseAmountMap = new HashMap<>();
    static {
        monsterBaseAmountMap.put(GameConfig.ENTITY_ID.SWORD_MAN, 3);
        monsterBaseAmountMap.put(GameConfig.ENTITY_ID.ASSASSIN, 3);
        monsterBaseAmountMap.put(GameConfig.ENTITY_ID.GIANT, 1);
        monsterBaseAmountMap.put(GameConfig.ENTITY_ID.BAT, 3);
        monsterBaseAmountMap.put(GameConfig.ENTITY_ID.NINJA, 3);
    }

    public static void readMonsterWaveConfigFromJson() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader("src/battle/config/conf/json/MonsterWaveScript.json");
            JsonObject jsonObject = (JsonObject) parser.parse(fileReader);
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                int waveId = Integer.parseInt(entry.getKey());
                JsonArray value = (JsonArray) entry.getValue();
                List<MonsterWaveSlot> monsterWaveSlotList = new ArrayList<>();
                for (JsonElement element : value) {
                    JsonObject jsonObject1 = (JsonObject) element;
//                    int monsterId = jsonObject1.get("monsterId").getAsInt();
//                    double rate = jsonObject1.get("rate").getAsDouble();
//                    String monsterClass = jsonObject1.get("monsterClass").getAsString();
//                    String category = jsonObject1.get("category").getAsString();
                    //                    MonsterWaveSlot monsterWaveSlot = new MonsterWaveSlot(monsterId, rate, monsterClass, category);
                    MonsterWaveSlot monsterWaveSlot = gson.fromJson(element, MonsterWaveSlot.class);
                    monsterWaveSlotList.add(monsterWaveSlot);
                }
                MonsterWaveScript monsterWaveScript = new MonsterWaveScript(monsterWaveSlotList);
                monsterWaveScriptHashMap.put(waveId, monsterWaveScript);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
