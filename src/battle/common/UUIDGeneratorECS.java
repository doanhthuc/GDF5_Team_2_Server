package battle.common;

import battle.config.GameConfig;

public class UUIDGeneratorECS {
    private final long PLAYER_START_ENTITY_ID = 0;
    private final long OPPONENT_START_ENTITY_ID = 100000;

    private final long TOWER_START_ENTITY_ID = 10;
    private final long SPELL_START_ENTITY_ID = 2000;
    private final long MONSTER_START_ENTITY_ID = 4000;
    private final long BULLET_START_ENTITY_ID = 10000;

    private long playerTowerEntityId;
    private long playerSpellEntityId;
    private long playerMonsterEntityId;
    private long playerBulletEntityId;

    private long opponentTowerEntityId;
    private long opponentSpellEntityId;
    private long opponentMonsterEntityId;
    private long opponentBulletEntityId;

    private long componentID = 0;
    private long systemID = 0;

    public UUIDGeneratorECS() {
        // set UUID for each type of Entity
        playerTowerEntityId = TOWER_START_ENTITY_ID + PLAYER_START_ENTITY_ID;
        playerSpellEntityId = SPELL_START_ENTITY_ID + PLAYER_START_ENTITY_ID;
        playerMonsterEntityId = MONSTER_START_ENTITY_ID + PLAYER_START_ENTITY_ID;
        playerBulletEntityId = BULLET_START_ENTITY_ID + PLAYER_START_ENTITY_ID;

        opponentTowerEntityId = TOWER_START_ENTITY_ID + OPPONENT_START_ENTITY_ID;
        opponentSpellEntityId = SPELL_START_ENTITY_ID + OPPONENT_START_ENTITY_ID;
        opponentMonsterEntityId = MONSTER_START_ENTITY_ID + OPPONENT_START_ENTITY_ID;
        opponentBulletEntityId = BULLET_START_ENTITY_ID + OPPONENT_START_ENTITY_ID;
    }

    public long genEntityID(EntityMode entityMode, int entityTypeID) {
        if (ValidatorECS.isEntityInGroupId(entityTypeID, GameConfig.GROUP_ID.TOWER_ENTITY))
            return genTowerEntityIdByMode(entityMode);

        if (ValidatorECS.isEntityInGroupId(entityTypeID, GameConfig.GROUP_ID.SPELl_ENTITY))
            return genSpellEntityIdByMode(entityMode);

        if (ValidatorECS.isEntityInGroupId(entityTypeID, GameConfig.GROUP_ID.MONSTER_ENTITY))
            return genMonsterEntityIdByMode(entityMode);

        if (ValidatorECS.isEntityInGroupId(entityTypeID, GameConfig.GROUP_ID.BULLET_ENTITY))
            return genBulletEntityIdByMode(entityMode);
        return 0;
    }

    public long genTowerEntityIdByMode(EntityMode entityMode) {
        if (entityMode == EntityMode.PLAYER) return ++playerTowerEntityId;
        else return ++opponentTowerEntityId;
    }

    public long genSpellEntityIdByMode(EntityMode entityMode) {
        if (entityMode == EntityMode.PLAYER) return ++playerSpellEntityId;
        else return ++opponentSpellEntityId;
    }

    public long genMonsterEntityIdByMode(EntityMode entityMode) {
        if (entityMode == EntityMode.PLAYER) return ++playerMonsterEntityId;
        else return ++opponentMonsterEntityId;
    }

    public long genBulletEntityIdByMode(EntityMode entityMode) {
        if (entityMode == EntityMode.PLAYER) return ++playerBulletEntityId;
        else return ++opponentBulletEntityId;
    }

    public long genComponentID() {
        return ++componentID;
    }

    public long getPlayerStartEntityID() {
        return PLAYER_START_ENTITY_ID;
    }

    public long getOpponentStartEntityID() {
        return OPPONENT_START_ENTITY_ID;
    }

    public long genSystemID() {
        return ++systemID;
    }

    public long getPlayerTowerEntityId() {
        return playerTowerEntityId;
    }

    public long getPlayerSpellEntityId() {
        return playerSpellEntityId;
    }

    public long getPlayerMonsterEntityId() {
        return playerMonsterEntityId;
    }

    public long getPlayerBulletEntityId() {
        return playerBulletEntityId;
    }

    public long getOpponentTowerEntityId() {
        return opponentTowerEntityId;
    }

    public long getOpponentSpellEntityId() {
        return opponentSpellEntityId;
    }

    public long getOpponentMonsterEntityId() {
        return opponentMonsterEntityId;
    }

    public long getOpponentBulletEntityId() {
        return opponentBulletEntityId;
    }
}
