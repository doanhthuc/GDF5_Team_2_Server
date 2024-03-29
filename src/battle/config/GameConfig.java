package battle.config;

import battle.common.EntityMode;
import battle.component.common.*;
import battle.component.effect.*;
import battle.component.info.LifeComponent;
import battle.component.info.SpellInfoComponent;
import battle.component.info.TowerInfoComponent;
import bitzero.core.I;

import java.util.Arrays;
import java.util.List;

public class GameConfig {
    public static boolean DEBUG = false;
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
        public static final int MONSTER_INFO = 1;
        public static final int TOWER_INFO = 2;
        public static final int BULLET_INFO = 3;
        public static final int LIFE = 4;
        public static final int POSITION = 5;
        public static final int VELOCITY = 6;
        public static final int APPEARANCE = 7;
        public static final int PATH = 8;
        public static final int COLLISION = 9;
        public static final int DAMAGE_EFFECT = 10;
        public static final int SLOW_EFFECT = 11;
        public static final int FROZEN_EFFECT = 12;
        public static final int ATTACK = 13;
        public static final int BUFF_ATTACK_SPEED = 14;
        public static final int BUFF_ATTACK_DAMAGE = 15;
        public static final int SPELL = 16;
        public static final int SKELETON = 17;
        public static final int UNDER_GROUND = 18;
        public static final int SPAWN_MINION = 19;
        public static final int HEALING_ABILITY = 20;
        public static final int SPRITE_SHEET = 21;
        public static final int TRAP_INFO = 22;
        public static final int TRAP_EFFECT = 23;
        public static final int TOWER_ABILITY = 24;
        public static final int BUFF_ATTACK_RANGE = 25;
        public static final int ACCELERATION = 26;
        public static final int FROG_BULLET_SKILL = 27;
        public static final int WIZARD_BULLET_SKILL = 28;
        public static final int DAMAGE_AMPLIFY_COMPONENT = 29;
        public static final int POISON = 30;
        public static final int SNAKE_BURN_HP_AURA = 31;
        public static final int GOAT_SLOW_EFFECT = 32;
        public static final int GOAT_SLOW_AURA = 33;
    }

    public static class HOUSE_POSITION {
        public static double x = 6;
        public static double y = 0;
    }

    public static class MONSTER_BORN_POSITION {
        public static double x = 0;
        public static double y = 4;
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
        public static final int SLOW_BULLET = 21;
        public static final int WIZARD_BULLET = 22;
        public static final int TREE = 23;
        public static final int HOLE = 24;
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
        public static final int TOWER_SPECIAL_SKILL = 15;
        public static final int SNAPSHOT_SYSTEM = 16;
    }

    public static class GROUP_ID {
        public static List<Integer> TOWER_ENTITY = Arrays.asList(ENTITY_ID.CANNON_TOWER, ENTITY_ID.WIZARD_TOWER, ENTITY_ID.FROG_TOWER, ENTITY_ID.BUNNY_TOWER, ENTITY_ID.SNAKE_TOWER, ENTITY_ID.GOAT_TOWER, ENTITY_ID.BEAR_TOWER);
        public static List<Integer> SPELl_ENTITY = Arrays.asList(ENTITY_ID.FIRE_SPELL, ENTITY_ID.FROZEN_SPELL, ENTITY_ID.TRAP_SPELL);
        public static List<Integer> ATTACK_TOWER_ENTITY = Arrays.asList(ENTITY_ID.CANNON_TOWER, ENTITY_ID.WIZARD_TOWER, ENTITY_ID.FROG_TOWER, ENTITY_ID.BUNNY_TOWER, ENTITY_ID.BEAR_TOWER);
        public static List<Integer> MONSTER_ENTITY = Arrays.asList(ENTITY_ID.SWORD_MAN, ENTITY_ID.DEMON_TREE, ENTITY_ID.DEMON_TREE_MINION, ENTITY_ID.ASSASSIN,
                ENTITY_ID.BAT, ENTITY_ID.NINJA, ENTITY_ID.GIANT, ENTITY_ID.DARK_GIANT, ENTITY_ID.SATYR);

        public static List<Integer> BULLET_ENTITY = Arrays.asList(ENTITY_ID.BULLET, ENTITY_ID.WIZARD_BULLET, ENTITY_ID.SLOW_BULLET);
        public static List<Integer> EFFECT_COMPONENT = Arrays.asList(COMPONENT_ID.DAMAGE_EFFECT, COMPONENT_ID.FROZEN_EFFECT, COMPONENT_ID.SLOW_EFFECT);
        public static List<Integer> INFO_COMPONENT = Arrays.asList(COMPONENT_ID.BULLET_INFO, COMPONENT_ID.TOWER_INFO, COMPONENT_ID.MONSTER_INFO);

        public static List<Integer> SNAPSHOT_COMPONENT = Arrays.asList(PathComponent.typeID, PositionComponent.typeID, LifeComponent.typeID,
                VelocityComponent.typeID, DamageEffect.typeID, SlowEffect.typeID,FrozenEffect.typeID, TrapEffect.typeID,
                UnderGroundComponent.typeID, HealingAbilityComponent.typeID, SpawnMinionComponent.typeID , FireBallEffect.typeID,
                TowerInfoComponent.typeID, AttackComponent.typeID, TowerAbilityComponent.typeID , SpellInfoComponent.typeID
                );
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
            public static String AIR = "aerial";
        }

        public static List<Integer> LAND_MONSTER = Arrays.asList(ENTITY_ID.SWORD_MAN, ENTITY_ID.ASSASSIN, ENTITY_ID.BAT, ENTITY_ID.NINJA, ENTITY_ID.GIANT);
        public static List<Integer> AIR_MONSTER = Arrays.asList(ENTITY_ID.BAT);
        public static List<Integer> BOSS_MONSTER = Arrays.asList(ENTITY_ID.DEMON_TREE, ENTITY_ID.DARK_GIANT, ENTITY_ID.SATYR);
    }

    public static class FROG_BULLET {
        public static int HIT_FIRST_TIME = 1;
        public static int HIT_SECOND_TIME = 2;
        public static int HIT_BOTH_TIME = 3;
    }

    public static class BATTLE_RESULT {
        public static int WIN = 0;
        public static int LOSE = 1;
        public static int DRAW = 2;
    }

    public static int WAVE_AMOUNT = 20;
    public static int PLAYER_HP = 20;
    public static int PLAYER_ENERGY = 30;
    public static int OPPONENT_ENERGY = 30;

    public static long DAILY_SHOP_RESET_TIME_SECOND = 20;

    public static class BATTLE {
        public static long START_GAME_AFTER = 8 * 1000;
        public static long WAVE_TIME = 20 * 1000;
        public static int AMOUNT_MONSTER_EACH_WAVE = 5;
        public static int TICK_RATE = 50;

        public static int WINNER_TROPHY = 10;
        public static int LOSER_TROPHY = 10;

        public static int DELAY_BUILD_TOWER = 1 * 1000;
        public static int TIME_MATCHING_BOT = 5 * 1000;
        public static double DELAY_SPELL = 0.3;
    }

    public static int TOWER_MAX_LEVEL = 3;
}

