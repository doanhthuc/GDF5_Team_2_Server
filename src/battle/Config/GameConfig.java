package battle.Config;

public class GameConfig {
    public static String PLAYER = "player";
    public static String OPPONENT = "opponent";

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
    }

    public static class ENTITY_ID {
        public static final int SWORD_MAN = 1;
        public static final int CANNON_TOWER = 2;
        public static final int BULLET = 3;
        public static final int BEAR_TOWER = 4;
        public static final int FROG_TOWER = 5;
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
    }

    public static int TILE_WIDTH = 77;

    public static int TILE_HEIGHT = 77;
    public static int MAP_WIDTH = 7;
    public static int MAP_HEIGHT = 5;
    public static int RIVER_MAP_HEIGHT = 100;
}

