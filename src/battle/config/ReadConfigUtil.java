package battle.config;

import battle.config.GameStat.MonsterConfigItem;
import battle.config.GameStat.TowerConfigItem;
import battle.config.GameStat.TowerStat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ReadConfigUtil {
    public final static HashMap<Integer, TowerConfigItem> towerInfo = new HashMap<>();
    public final static MonsterConfigItem monsterInfo = new MonsterConfigItem();

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

    public static class MONSTER_IN_CONFIG {
        public static int SWORD_MAN = 0;
        public static int ASSASSIN = 1;
        public static int GIANT = 2;
        public static int BAT = 3;
        public static int NINJA = 4;
        public static int DARK_GIANT = 5;
        public static int SATYR = 6;
        public static int DEMON_TREE_BOSS = 11;
        public static int DEMON_TREE_MINION = 12;
    }

    public static void readMonsterConfig() {
        JsonParser parser = new JsonParser();
        try {
            FileReader fileReader = new FileReader("src/battle/config/json/Monster.json");
            JsonObject jsonObject = (JsonObject) parser.parse(fileReader);
            JsonObject multiplierObj = jsonObject.getAsJsonObject("multiplier");

            for (Map.Entry<String, JsonElement> entryMultiplier : multiplierObj.entrySet()) {
                int key = Integer.parseInt(entryMultiplier.getKey());

                JsonObject value = (JsonObject) entryMultiplier.getValue();
                double hp = 0;
                int numberMonsters = 0;
                if (value.has("hp")) hp = value.get("hp").getAsDouble();
                if (value.has("numberMonsters")) numberMonsters = value.get("numberMonsters").getAsInt();
                monsterInfo.addMonsterMultiplier(key, new MonsterConfigItem.MonsterMultiplier(hp, numberMonsters));
            }

            JsonObject monsterStatObj = jsonObject.getAsJsonObject("monster");
            for (Map.Entry<String, JsonElement> monsterStatEntry : monsterStatObj.entrySet()) {
                {
                    int monsterType = Integer.parseInt(monsterStatEntry.getKey());
                    JsonObject statValue = (JsonObject) monsterStatEntry.getValue();
                    String name = "", category = "", classs = "";
                    double hp = 0, speed = 0, hitRadius = 0;
                    int weight = 0, ability = 0, energy = 0, gainEnergy = 0, numberMonster = 0;
                    try {
                        if (statValue.has("name")) name = statValue.get("name").getAsString();
                        if (statValue.has("category")) category = statValue.get("category").getAsString();
                        if (statValue.has("classs")) classs = statValue.get("classs").getAsString();
                        if (statValue.has("hp")) hp = statValue.get("hp").getAsDouble();
                        if (statValue.has("speed")) speed = statValue.get("speed").getAsDouble();
                        if (statValue.has("hitRadius")) hitRadius = statValue.get("hitRadius").getAsDouble();
                        if (statValue.has("weight")) weight = statValue.get("weight").getAsInt();
                        if (statValue.has("ability")) ability = statValue.get("ability").getAsInt();
                        if (statValue.has("energy")) energy = statValue.get("energy").getAsInt();
                        if (statValue.has("gainEnergy")) gainEnergy = statValue.get("gainEnergy").getAsInt();
                        if (statValue.has("numberMonster")) numberMonster = statValue.get("numberMonster").getAsInt();
                    } catch (Exception e) {

                    }
                    MonsterConfigItem.MonsterStat monsterStat = new MonsterConfigItem.MonsterStat(name, category, classs, hp, speed, hitRadius, weight, ability, energy, gainEnergy, numberMonster);
                    monsterInfo.addMonsterStat(monsterType, monsterStat);
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}