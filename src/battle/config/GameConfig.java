package battle.config;

import battle.common.EntityMode;

import java.util.Arrays;
import java.util.List;

public class GameConfig {
    public static boolean DEBUG = true;
    public static int TILE_WIDTH = 77;
    public static int TILE_HEIGHT = 77;
    public static int MAP_WIDTH = 7;
    public static int MAP_HEIGHT = 5;
    public static int RIVER_MAP_HEIGHT = 100;

    public static class MAP {
        public static int NONE = 0;
        public static int ATTACK_SPEED = 1;
        public static int ATTACK_RANGE = 2;
        public static int ATTACK_DAMAGE = 3;
        public static int TREE = 5;
        public static int HOLE = 6;
        public static int TOWER = 7;

    }

    public static class COMPONENT_ID {
        public static int MONSTER_INFO = 1;
        public static int TOWER_INFO = 2;
        public static int BULLET_INFO = 3;
        public static int LIFE = 4;
        public static int POSITION = 5;
        public static int VELOCITY = 6;
        public static int APPEARANCE = 7;
        public static int PATH = 8;
        public static int COLLISION = 9;
        public static int DAMAGE_EFFECT = 10;
        public static int SLOW_EFFECT = 11;
        public static int FROZEN_EFFECT = 12;
        public static int ATTACK = 13;
        public static int BUFF_ATTACK_SPEED = 14;
        public static int BUFF_ATTACK_DAMAGE = 15;
        public static int SPELL = 16;
        public static int SKELETON = 17;
        public static int UNDER_GROUND = 18;
        public static int SPAWN_MINION = 19;
        public static int HEALING_ABILITY = 20;
        public static int SPRITE_SHEET = 21;
        public static int TRAP_INFO = 22;
        public static int TRAP_EFFECT = 23;
        public static int TOWER_ABILITY = 24;
        public static int BUFF_ATTACK_RANGE = 25;
        public static int ACCELERATION = 26;

    }

    public static class HOUSE_POSITION {
        public static double x = 6;
        public static double y = 0;
    }

    public static class ENTITY_ID {
        public static final int CANNON_TOWER = 0;
        public static final int WIZARD_TOWER = 1;
        public static final int FROG_TOWER = 2;
        public static final int BUNNY_TOWER = 3;
        public static final int BEAR_TOWER = 4;
        public static final int GOAT_TOWER = 5;
        public static final int SNAKE_TOWER = 6;
        public static final int FIRE_SPELL = 7;
        public static final int FROZEN_SPELL = 8;
        public static final int TRAP_SPELL = 9;

        public static final int ASSASSIN = 11;
        public static final int BAT = 12;
        public static final int GIANT = 13;
        public static final int NINJA = 14;
        public static final int DEMON_TREE = 15;
        public static final int DEMON_TREE_MINION = 16;
        public static final int DARK_GIANT = 17;
        public static final int SATYR = 18;
        public static final int SWORD_MAN = 19;

        public static final int BULLET = 20;
    }

    public static class TOWER_TARGET_STRATEGY {
        public static final int MAX_HP = 1;
        public static final int MIN_HP = 2;
        public static final int MAX_DISTANCE = 3;
        public static final int MIN_DISTANCE = 4;
    }

    public static class SYSTEM_ID {
        public static final int MOVEMENT = 1;
        public static final int PATH_MONSTER = 2;
        public static final int RENDER = 3;
        public static final int LIFE = 4;
        public static final int ATTACK = 5;
        public static final int EFFECT = 6;
        public static final int SPRITE_SHEET = 7;
        public static final int SPELL = 8;
        public static final int SKELETON = 9;
        public static final int BULLET = 10;
        public static final int MONSTER = 11;
        public static final int ABILITY = 12;
        public static final int COLLISION = 13;
        public static final int RESET_SYSTEM = 14;
    }

    public static class GROUP_ID {
        public static List<Integer> TOWER_ENTITY = Arrays.asList(ENTITY_ID.BEAR_TOWER, ENTITY_ID.BEAR_TOWER, ENTITY_ID.CANNON_TOWER);
        public static List<Integer> MONSTER_ENTITY = Arrays.asList(ENTITY_ID.SWORD_MAN, ENTITY_ID.DEMON_TREE, ENTITY_ID.DEMON_TREE_MINION, ENTITY_ID.ASSASSIN,
                ENTITY_ID.BAT, ENTITY_ID.NINJA, ENTITY_ID.GIANT, ENTITY_ID.DARK_GIANT, ENTITY_ID.SATYR);
        public static List<Integer> BULLET_ENTITY = Arrays.asList(ENTITY_ID.BULLET);
        public static List<Integer> EFFECT_COMPONENT = Arrays.asList(COMPONENT_ID.DAMAGE_EFFECT, COMPONENT_ID.FROZEN_EFFECT, COMPONENT_ID.SLOW_EFFECT);
        public static List<Integer> INFO_COMPONENT = Arrays.asList(COMPONENT_ID.BULLET_INFO, COMPONENT_ID.TOWER_INFO, COMPONENT_ID.MONSTER_INFO);
    }

    public static class COMPONENT_NAME {
        public static List<String> NAME = Arrays.asList("", "MONSTER_INFO", "TOWER_INFO", "BULLET_INFO", "LIFE", "POSITION", "VELOCITY", "APPEARANCE", "PATH", "COLLISION", "DAMAGE_EFFECT", "SLOW_EFFECT",
                "FROZEN_EFFECT", "ATTACK", "BUFF_ATTACK_SPEED", "BUFF_ATTACK_DAMAGE");
    }

    public static class MONSTER {
        public static class CATEGORY {
            public static String NORMAL = "normal";
            public static String BOSS = "boss";
        }

        public static class CLASS {
            public static String LAND = "land";
            public static String AIR = "air";
        }
    }

    public static class FROG_BULLET {
        public static int HIT_FIRST_TIME = 1;
        public static int HIT_SECOND_TIME = 2;
        public static int HIT_BOTH_TIME = 3;

    }

    public static class BATTLE_RESULT {
        public static int WIN = 0;
        public static int LOSE = 0;
        public static int DRAW = 0;
    }

    public static int WAVE_AMOUNT = 20;
    public static int PLAYER_HP = 1;
    public static int PLAYER_ENERGY = 30;
}

