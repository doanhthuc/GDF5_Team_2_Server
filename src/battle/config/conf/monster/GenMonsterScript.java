package battle.config.conf.monster;

import bitzero.util.common.business.Debug;
import com.google.gson.*;

import java.io.FileReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GenMonsterScript {
    public static final GenMonsterScript INS = new GenMonsterScript();
    private final Map<Short, GenMonsterScriptItem[]> scriptMap = new HashMap<>();
    private final Map<String, int[]> typeList = new HashMap<>();

    public static void main(String[] args) {
       GenMonsterScriptItem[] list = GenMonsterScript.INS.getScriptByWaveNum((short) 20);
       for (int i = 0; i < list.length; i++) {
           System.out.println(list[i]);
       }
    }

    private GenMonsterScript() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("src/battle/config/conf/json/***.json");
            JsonObject obj = (JsonObject) parser.parse(reader);

            JsonObject scriptObj = obj.getAsJsonObject("script");
            for (Map.Entry<String, JsonElement> entry : scriptObj.entrySet()){
                short key = Short.parseShort(entry.getKey());
                JsonArray array = (JsonArray) entry.getValue();
                GenMonsterScriptItem[] itemList = new GenMonsterScriptItem[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    JsonObject value = (JsonObject) array.get(i);
                    GenMonsterScriptItem item = gson.fromJson(value.toString(), GenMonsterScriptItem.class);
                    itemList[i] = item;
                }
                scriptMap.put(key, itemList);
            }
        } catch (Exception e){
            Debug.trace(e);
        }
    }

    public GenMonsterScriptItem[] getScriptByWaveNum(short waveNum) {
        return  scriptMap.get(waveNum);
    }

    public static class GenMonsterScriptItem implements Serializable {
        short id;
        double rate;
        String monsterClass;
        String category;

        public short getId() {
            return id;
        }

        public double getRate() {
            return rate;
        }

        public String getMonsterClass() {
            return monsterClass;
        }

        public String getCategory() {
            return category;
        }

        @Override
        public String toString() {
            return "GenMonsterScriptItem{" +
                    "id=" + id +
                    ", rate=" + rate +
                    ", monsterClass='" + monsterClass + '\'' +
                    ", category='" + category + '\'' +
                    '}';
        }
    }
}
