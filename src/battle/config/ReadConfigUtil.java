package battle.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class ReadConfigUtil {
    public final static HashMap<Integer, TowerConfigItem> towerInfo = new HashMap<>();
    public static class TOWER_IN_CONFIG {
        public static int CANNON = 0;
        public static int WIZARD = 1;
        public static int FROG = 2;
        public static int BUNNY = 3;
        public static int BEAR = 4;
        public static int GOAT = 5;
        public static int SNAKE = 6;
    }
    public static void readTowerConfig() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader("src/battle/config/json/Tower.json");
            JsonObject jsonObject = (JsonObject) parser.parse(fileReader);
            JsonObject towerObj = jsonObject.getAsJsonObject("tower");

            for (Map.Entry<String, JsonElement> entry : towerObj.entrySet()) {
                int key = Integer.parseInt(entry.getKey());
                JsonObject value = (JsonObject) entry.getValue();
                HashMap<Integer, TowerStat> towerStatHashMap = new HashMap<>();
                String name = "", archetype = "", targetType = "", bulletType = "";
                int bulletTargetBuffType = -1, energy = 0, attackAnimationTime = 0, shootAnimationTime = 0;
                if (value.has("name")) name = value.get("name").getAsString();
                if (value.has("archetype")) archetype = value.get("archetype").getAsString();
                if (value.has("targetType")) targetType = value.get("targetType").getAsString();
                if (value.has("bulletType")) bulletType = value.get("bulletType").getAsString();
                if (value.has("bulletTargetBuffType"))
                    bulletTargetBuffType = value.get("bulletTargetBuffType").getAsInt();
                if (value.has("energy")) energy = value.get("energy").getAsInt();
                if (value.has("attackAnimationTime")) attackAnimationTime = value.get("attackAnimationTime").getAsInt();
                if (value.has("shootAnimationTime")) shootAnimationTime = value.get("shootAnimationTime").getAsInt();

                JsonObject stat = value.getAsJsonObject("stat");
                for (Map.Entry<String, JsonElement> statEntry : stat.entrySet()) {
                    {
                        int towerRank = Integer.parseInt(statEntry.getKey());
                        JsonObject statValue = (JsonObject) statEntry.getValue();
                        double damage = 0, attackSpeed = 0, range = 0, bulletRadius = 0, bulletSpeed = 0;
                        try {
                            if (statValue.has("damage")) damage = statValue.get("damage").getAsDouble();
                            if (statValue.has("attackSpeed")) attackSpeed = statValue.get("attackSpeed").getAsDouble();
                            if (statValue.has("range")) range = statValue.get("range").getAsDouble();
                            if (statValue.has("bulletRadius"))
                                bulletRadius = statValue.get("bulletRadius").getAsDouble();
                            if (statValue.has("bulletSpeed")) bulletSpeed = statValue.get("bulletSpeed").getAsDouble();
                        } catch (Exception e) {

                        }
                        TowerStat towerStat = new TowerStat(damage, attackSpeed, range, bulletRadius, bulletSpeed);
                        towerStatHashMap.put(towerRank, towerStat);
                    }
                }
                TowerConfigItem towerConfigItem = new TowerConfigItem(name, archetype, targetType, bulletType, bulletTargetBuffType, energy,
                        attackAnimationTime, shootAnimationTime, towerStatHashMap);
                towerInfo.put(key, towerConfigItem);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
