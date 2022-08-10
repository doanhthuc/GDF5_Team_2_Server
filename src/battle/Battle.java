package battle;

import battle.common.*;
import battle.component.common.AttackComponent;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.effect.EffectComponent;
import battle.component.effect.FrozenEffect;
import battle.component.effect.SlowEffect;
import battle.component.effect.TowerAbilityComponent;
import battle.component.info.MonsterInfoComponent;
import battle.config.GameConfig;
import battle.config.GameStat.TargetBuffConfigItem2;
import battle.config.GameStat.TowerConfigItem2;
import battle.config.GameStat.TowerStat2;
import battle.config.MonsterWaveConfig;
import battle.config.ReadConfigUtil;
import battle.config.conf.targetBuff.TargetBuffConfig;
import battle.config.conf.targetBuff.TargetBuffConfigItem;
import battle.config.conf.tower.TowerConfig;
import battle.config.conf.tower.TowerConfigItem;
import battle.config.conf.tower.TowerStat;
import battle.config.conf.towerBuff.TowerBuffConfig;
import battle.entity.EntityECS;
import battle.factory.ComponentFactory;
import battle.factory.EntityFactory;
import battle.manager.ComponentManager;
import battle.manager.EntityManager;
import battle.newMap.BattleMapObject;
import battle.pool.ComponentPool;
import battle.pool.EntityPool;
import battle.system.*;
import model.PlayerInfo;

import java.util.*;

public class Battle {
    private HashMap<Integer, EntityMode> entityModeByPlayerID = new HashMap<>();
    private HashMap<Integer, BattleMap> battleMapListByPlayerId = new HashMap<>();
    //Pool and Manager
    private ComponentPool componentPool;
    private EntityPool entityPool;
    private EntityManager entityManager;


    private ComponentManager componentManager;
    private ComponentFactory componentFactory;
    private EntityFactory entityFactory;
    //System
    public AttackSystem attackSystem;
    public MovementSystem movementSystem;
    public PathMonsterSystem pathMonsterSystem;
    public CollisionSystem collisionSystem;
    public EffectSystem effectSystem;
    public LifeSystem lifeSystem;
    public AbilitySystem abilitySystem;
    public BulletSystem bulletSystem;
    public ResetSystem resetSystem;
    public MonsterSystem monsterSystem;
    public SpellSystem spellSystem;
    //Map
    public BattleMap player1BattleMap;
    public List<Point>[][] player1ShortestPath;
    public BattleMap player2BattleMap;
    public List<Point>[][] player2ShortestPath;
    //MonsterWave
    private final int waveAmount = GameConfig.WAVE_AMOUNT;
    private int currentWave = 0;
    private List<List<Integer>> monsterWave;
    private long nextWaveTime;
    private long nextBornMonsterTime;
    //PlayerHp and Energy
    public int player1HP = GameConfig.PLAYER_HP;
    public int player2HP = GameConfig.PLAYER_HP;
    private int player1energy = GameConfig.PLAYER_ENERGY;
    private int player2energy = GameConfig.OPPONENT_ENERGY;
    public PlayerInfo user1;
    public PlayerInfo user2;

    public Battle(PlayerInfo userId1, PlayerInfo userId2) {
        this.initPoolAndManager();
        this.initSystem();
        this.initMap(userId1, userId2);
        this.initMonsterWave();
    }


    //init
    public void initPoolAndManager() {
        this.entityPool = new EntityPool();
        this.componentPool = new ComponentPool();
        this.entityManager = new EntityManager();
        this.componentManager = new ComponentManager();
        this.componentFactory = new ComponentFactory(this.componentManager, this.componentPool);
        this.entityFactory = new EntityFactory(this.entityManager, this.componentFactory, this.entityPool, this);
    }

    public void initSystem() {
        this.attackSystem = new AttackSystem();
        this.pathMonsterSystem = new PathMonsterSystem();
        this.movementSystem = new MovementSystem();
        this.collisionSystem = new CollisionSystem();
        this.effectSystem = new EffectSystem();
        this.lifeSystem = new LifeSystem();
        this.abilitySystem = new AbilitySystem();
        this.bulletSystem = new BulletSystem();
        this.resetSystem = new ResetSystem();
        this.monsterSystem = new MonsterSystem();
        this.spellSystem = new SpellSystem();
    }

    public void initMap(PlayerInfo user1, PlayerInfo user2) {
        this.player1BattleMap = new BattleMap();
        this.player1ShortestPath = FindPathUtils.findShortestPathForEachTile(player1BattleMap.map);

        this.player2BattleMap = new BattleMap();
        this.player2ShortestPath = FindPathUtils.findShortestPathForEachTile(player2BattleMap.map);

        this.battleMapListByPlayerId.put(user1.getId(), this.player1BattleMap);
        this.battleMapListByPlayerId.put(user2.getId(), this.player2BattleMap);

        this.entityModeByPlayerID.put(user1.getId(), EntityMode.PLAYER);
        this.entityModeByPlayerID.put(user2.getId(), EntityMode.OPPONENT);

        this.user1 = user1;
        this.user2 = user2;
        // DEBUG
//        for (int j = BattleMap.mapH-1; j >= 0; j--)
//        {
//            for (int i = 0; i < BattleMap.mapW; i++) {
//                if (FindPathUtils.findPathAble(battleMap.map[i][j]))
//                {
//                    for (Point point : shortestPath[i][j]){
//                        System.out.print((int)point.getX()+" "+(int)point.getY()+"||");
//                    }
//                }
//                System.out.println();
//            }
//            System.out.println("================================");
//        }
    }

    public void initMonsterWave() {
        this.monsterWave = this.createNewMonsterWave();
        System.out.println("monsterWave:" + this.monsterWave);
        for (int i = 0; i < MonsterWaveConfig.monsterWaveScriptHashMap.size(); i++) {
            for (int j = 0; j < this.monsterWave.get(i).size(); j++) {
                System.out.print(this.monsterWave.get(i).get(j) + " ");
            }
            System.out.println();
        }
        this.currentWave = -1;
    }

    //Update

    public void updateSystem() throws Exception {
        if (GameConfig.DEBUG) this.debug();
        resetSystem.run(this);
        abilitySystem.run(this);
        effectSystem.run(this);
        attackSystem.run(this);
        lifeSystem.run(this);
        collisionSystem.run(this);
        pathMonsterSystem.run(this);
        spellSystem.run(this);
        monsterSystem.run(this);
        bulletSystem.run(this);
        movementSystem.run(this);
    }

    public void updateMonsterWave() throws Exception {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= this.nextWaveTime) {
            this.currentWave += 1;
            this.nextWaveTime += GameConfig.BATTLE.WAVE_TIME;
        }
        if ((this.currentWave < 0) || (this.currentWave > this.waveAmount)) return;
        List<Integer> currentWaveList = this.monsterWave.get(this.currentWave);
        //System.out.println(this.nextBornMonsterTime - currentTime);
        if (currentTime >= this.nextBornMonsterTime && (currentWaveList.size() > 0)) {
            this.bornMonsterByMonsterID(this.monsterWave.get(this.currentWave).get(currentWaveList.size() - 1), EntityMode.PLAYER);
            this.bornMonsterByMonsterID(this.monsterWave.get(this.currentWave).get(currentWaveList.size() - 1), EntityMode.OPPONENT);
            this.monsterWave.get(currentWave).remove(currentWaveList.size() - 1);
            this.nextBornMonsterTime = currentTime + 1000;
        }
    }

    //PlayerHp and Energy

    public void minusPlayerHP(int hp, EntityMode mode) {
        if (mode == EntityMode.PLAYER) this.player1HP -= hp;
        else this.player2HP -= hp;
    }

    public void addPlayerEnergy(int energy, EntityMode mode) {
        if (mode == EntityMode.PLAYER) this.player1energy += energy;
        else this.player2energy += energy;
    }

    public void minusPlayerEnergy(int energy, EntityMode mode) {
        if (mode == EntityMode.PLAYER) this.player1energy -= energy;
        else this.player2energy -= energy;
    }

    public List<Integer> createMonsterWaveByCurrentWaveId(int currentWave, EntityMode monde) {
        List<Integer> monsterWaveIdList = new ArrayList<>();
        MonsterWaveScript monsterWaveScript = MonsterWaveConfig.monsterWaveScriptHashMap.get(currentWave);
        for (int i = 0; i < monsterWaveScript.getMonsterWaveSlotList().size(); i++) {
            MonsterWaveSlot monsterWaveSlot = monsterWaveScript.getMonsterWaveSlotList().get(i);
            if (Objects.equals(monsterWaveSlot.getCategory(), "boss")) {
                int bossId = GameConfig.MONSTER.BOSS_MONSTER.get(new Random().nextInt(GameConfig.MONSTER.BOSS_MONSTER.size()));
                monsterWaveIdList.add(bossId);
                continue;
            }
            int sumAllTowerInMap = getAllCurrentTowerLevelInMap(monde);
            double monsterRate = monsterWaveSlot.getRate();
            int monsterBaseAmount = 1;
            int monsterId = -1;
            if (monsterWaveSlot.getMonsterId() != -1 && Objects.equals(monsterWaveSlot.getCategory(), "normal")) {
                monsterId = monsterWaveSlot.getMonsterId();
                monsterBaseAmount = MonsterWaveConfig.monsterBaseAmountMap.get(monsterId);
            } else if (monsterWaveSlot.getMonsterId() == -1 && Objects.equals(monsterWaveSlot.getMonsterClass(), GameConfig.MONSTER.CLASS.LAND)) {
                Random random = new Random();
                monsterId = GameConfig.MONSTER.LAND_MONSTER.get(random.nextInt(GameConfig.MONSTER.LAND_MONSTER.size()));
                monsterBaseAmount = MonsterWaveConfig.monsterBaseAmountMap.get(monsterId);
            } else if (Objects.equals(monsterWaveSlot.getMonsterClass(), GameConfig.MONSTER.CLASS.AIR)) {
                monsterId = GameConfig.MONSTER.AIR_MONSTER.get(new Random().nextInt(GameConfig.MONSTER.AIR_MONSTER.size()));
            }
            int multiplier = getMonsterAmountMultiplierByTowerLevel(sumAllTowerInMap);
            int monsterAmount = (int) Math.ceil(monsterBaseAmount * monsterRate * multiplier);
            for (int j = 0; j < monsterAmount; j++) {
                monsterWaveIdList.add(monsterId);
            }
        }
        return monsterWaveIdList;
    }

    public int getMonsterAmountMultiplierByTowerLevel(int level) {
        int multiplier = 1;
        if (level < 5) {
            multiplier = 1;
        } else if (level < 10) {
            multiplier = 2;
        } else if (level < 15) {
            multiplier = 3;
        } else if (level < 20) {
            multiplier = 4;
        } else if (level < 25) {
            multiplier = 5;
        } else if (level < 30) {
            multiplier = 6;
        } else {
            multiplier = 7;
        }
        return multiplier;
    }

    public int getAllCurrentTowerLevelInMap(EntityMode mode) {
        int level = 0;
        for (int i = 0; i < BattleMap.mapW; i++) {
            for (int j = 0; j < BattleMap.mapH; j++) {
                if (this.getBattleMapByEntityMode(mode).battleMapObject.isHavingTowerInTile(i, j)) {
                    level += this.getBattleMapByEntityMode(mode).battleMapObject.getTowerInTile(i, j).getLevel();
                }
            }
        }
        return level;
    }

    public List<List<Integer>> createNewMonsterWave2() {
        List<List<Integer>> monsterWave = new ArrayList<>();
        for (int i = 0; i < MonsterWaveConfig.monsterWaveScriptHashMap.size(); i++) {
            monsterWave.add(createMonsterWaveByCurrentWaveId(i + 1, EntityMode.PLAYER));
        }
        for (int i = 0; i < monsterWave.size(); i++) {
            System.out.println(monsterWave.get(i).size());
        }
        System.out.println("[Battle.java line 268] monsterWave: size " + monsterWave.size());
        return monsterWave;
    }


    public List<List<Integer>> createNewMonsterWave() {
        List<List<Integer>> monsterWave = new ArrayList<>(this.waveAmount);
        for (int waveIdx = 1; waveIdx <= this.waveAmount; waveIdx++) {
            List<Integer> wave = new ArrayList<>();
            int swordManAmount, batAmount, ninjaAmount, assassinAmount;
//            int monsterAmountInWave = Math.min(5 + waveIdx, 15);
//            swordManAmount = (int) Math.floor(Math.random() * monsterAmountInWave);
//            batAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount));
//            ninjaAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount - batAmount));
//            assassinAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount - batAmount - ninjaAmount));
            int monsterAmountInWave = GameConfig.BATTLE.AMOUNT_MONSTER_EACH_WAVE;
            swordManAmount = (int) Math.floor(Math.random() * monsterAmountInWave);
            batAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount));
            ninjaAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount - batAmount));
            assassinAmount = (monsterAmountInWave - swordManAmount - batAmount - ninjaAmount);

            for (int i = 1; i <= batAmount; i++)
                wave.add(GameConfig.ENTITY_ID.BAT);

            for (int i = 1; i <= ninjaAmount; i++)
                wave.add(GameConfig.ENTITY_ID.NINJA);

            for (int i = 1; i <= assassinAmount; i++)
                wave.add(GameConfig.ENTITY_ID.ASSASSIN);

            for (int i = 1; i <= swordManAmount; i++)
                wave.add(GameConfig.ENTITY_ID.SWORD_MAN);

            if (waveIdx == 5) {
                wave.add(GameConfig.ENTITY_ID.SATYR);
            }
            if (waveIdx == 10) {
                wave.add(GameConfig.ENTITY_ID.DARK_GIANT);
            }
            if (waveIdx == 15) {
                wave.add(GameConfig.ENTITY_ID.DEMON_TREE);
            }
            if (waveIdx == 20) {
                wave.add(GameConfig.ENTITY_ID.DEMON_TREE);
                wave.add(GameConfig.ENTITY_ID.DARK_GIANT);
            }
            monsterWave.add(wave);
        }
        return monsterWave;
    }

    public void bornMonsterByMonsterID(int monsterID, EntityMode mode) throws Exception {
        int tilePosX = 0;
        int tilePosY = 4;
        Point pixelPos = Utils.tile2Pixel(tilePosX, tilePosY, mode);
        switch (monsterID) {
            case GameConfig.ENTITY_ID.SWORD_MAN:
                this.entityFactory.createSwordManMonster(pixelPos, mode);
                break;
            case GameConfig.ENTITY_ID.ASSASSIN:
                this.entityFactory.createAssassinMonster(pixelPos, mode);
                break;
            case GameConfig.ENTITY_ID.NINJA:
                this.entityFactory.createNinjaMonster(pixelPos, mode);
                break;
            case GameConfig.ENTITY_ID.GIANT:
                this.entityFactory.createGiantMonster(pixelPos, mode);
                break;
            case GameConfig.ENTITY_ID.BAT:
                this.entityFactory.createBatMonster(pixelPos, mode);
                break;
            case GameConfig.ENTITY_ID.DARK_GIANT:
                this.entityFactory.createDarkGiantBoss(pixelPos, mode);
                break;
            case GameConfig.ENTITY_ID.SATYR:
                this.entityFactory.createSatyrBoss(pixelPos, mode);
                break;
            case GameConfig.ENTITY_ID.DEMON_TREE:
                this.entityFactory.createDemonTreeBoss(pixelPos, mode);
                break;
        }
    }

    public void buildTowerByTowerID(int towerID, int tilePosX, int tilePosY, EntityMode mode) throws Exception {
        EntityECS tower = null;
        switch (towerID) {
            case GameConfig.ENTITY_ID.CANNON_TOWER:
                tower = this.entityFactory.createCannonOwlTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.FROG_TOWER:
                tower = this.entityFactory.createFrogTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.WIZARD_TOWER:
                tower = this.entityFactory.createWizardTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.BEAR_TOWER:
                tower = this.entityFactory.createIceGunPolarBearTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.BUNNY_TOWER:
                tower = this.entityFactory.createBunnyOilGunTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.SNAKE_TOWER:
                tower = this.entityFactory.createSnakeAttackSpeedTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.GOAT_TOWER:
                tower = this.entityFactory.createGoatAttackDamageTower(new Point(tilePosX, tilePosY), mode);
                break;
        }

        assert tower != null;
        long entityID = tower.getId();
        this.updateMapWhenPutTower(entityID, towerID, tilePosX, tilePosY, mode);
        this.handlerPutTower(mode);
    }

    public void castSpellBySpellID(int spellID, double pixelPosX, double pixelPosY, EntityMode mode) throws Exception {
        switch (spellID) {
            case GameConfig.ENTITY_ID.FIRE_SPELL:
                this.entityFactory.createFireSpell(new Point(pixelPosX, pixelPosY), mode);
                break;
            case GameConfig.ENTITY_ID.FROZEN_SPELL:
                this.entityFactory.createFrozenSpell(new Point(pixelPosX, pixelPosY), mode);
                break;
            case GameConfig.ENTITY_ID.TRAP_SPELL:
                this.entityFactory.createTrapSpell(new Point(pixelPosX, pixelPosY), mode);
                break;
        }
    }

    public void updateMapWhenPutTower(long entityId, int towerId, int tilePosX, int tilePosY, EntityMode mode) throws Exception {
        if (mode == EntityMode.PLAYER) {
            this.player1BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;

            this.player1BattleMap.battleMapObject.putTowerIntoMap(entityId, new java.awt.Point(tilePosX, tilePosY), towerId);
        } else {
            this.player2BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;
            this.player2BattleMap.battleMapObject.putTowerIntoMap(entityId, new java.awt.Point(tilePosX, tilePosY), towerId);
        }

    }

    public void handlerDesTroyTower(int tilePosX, int tilePosY, EntityMode mode) {
        BattleMap battleMap = null;
        if (mode == EntityMode.PLAYER) {
            this.player1BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.NONE;
            battleMap = this.player1BattleMap;
        } else {
            this.player2BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.NONE;
            battleMap = this.player2BattleMap;
        }
        BattleMapObject battleMapObject = battleMap.battleMapObject;
        //TODO: DestroyTower in BattleMapObject
    }


    public void handlerPutTower(EntityMode mode) {
        if (mode == EntityMode.PLAYER)
            this.player1ShortestPath = FindPathUtils.findShortestPathForEachTile(player1BattleMap.map);
        else
            this.player2ShortestPath = FindPathUtils.findShortestPathForEachTile(player2BattleMap.map);

        List<EntityECS> monsterList = this.getEntityManager().getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID, PathComponent.typeID));
        for (EntityECS monster : monsterList) {
            if (monster.getMode() == mode) {
                PathComponent pathComponent = (PathComponent) monster.getComponent(PathComponent.typeID);
                PositionComponent positionComponent = (PositionComponent) monster.getComponent(PositionComponent.typeID);
                if (positionComponent != null) {
                    List<Point> path;
                    Point tilePos = Utils.pixel2Tile(positionComponent.getX(), positionComponent.getY(), mode);
                    if (monster.getMode() == EntityMode.PLAYER) {
                        path = this.player1ShortestPath[(int) tilePos.getX()][(int) tilePos.getY()];
                    } else {
                        path = this.player2ShortestPath[(int) tilePos.getX()][(int) tilePos.getY()];
                    }
                    if (path != null) {
                        List<Point> newPath = Utils.tileArray2PixelCellArray(path, mode);
                        pathComponent.setPath(newPath);
                        pathComponent.setCurrentPathIDx(0);
                    }
                }
            }
        }
    }

    public void handleUpgradeTower(long entityId, int towerLevel) throws Exception {
        this.onUpgradeTower(entityId, towerLevel);
    }

    public void onUpgradeTower(long entityId, int towerLevel) throws Exception {
        System.out.println("[Battle.java line 456] onUpgradeTower: " + entityId + " " + towerLevel);
        short level= (short)towerLevel;
        EntityECS entity = this.entityManager.getEntity(entityId);
        switch (entity.getTypeID()) {
            case GameConfig.ENTITY_ID.CANNON_TOWER:
            case GameConfig.ENTITY_ID.FROG_TOWER:
            case GameConfig.ENTITY_ID.WIZARD_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem bunnyOilConfig = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = bunnyOilConfig.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();
                List<EffectComponent> effectComponents = new ArrayList<>();
                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                break;
            }
            case GameConfig.ENTITY_ID.BEAR_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem bunnyOilConfig = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = bunnyOilConfig.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();

                TargetBuffConfigItem targetBuffConfigItem = TargetBuffConfig.INS.getTargetBuffConfig(TargetBuffConfig.BULLET_OIL_GUN);
                double duration = targetBuffConfigItem.getDuration().get(level) / 1000.0;

                FrozenEffect frozenEffect = this.componentFactory.createFrozenEffect(duration);

                List<EffectComponent> effectComponents = Arrays.asList(frozenEffect);

                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                break;
            }
            case GameConfig.ENTITY_ID.BUNNY_TOWER: {
                AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
                TowerConfigItem bunnyOilConfig = TowerConfig.INS.getTowerConfig((short) entity.getTypeID());
                TowerStat towerStat = bunnyOilConfig.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double bulletSpeed = towerStat.getBulletSpeed() * (GameConfig.TILE_WIDTH / 10.0);
                double attackSpeed = towerStat.getAttackSpeed() / 1000.0;
                double bulletRadius = towerStat.getBulletRadius() * GameConfig.TILE_WIDTH;
                double damage = towerStat.getDamage();

                TargetBuffConfigItem targetBuffConfigItem = TargetBuffConfig.INS.getTargetBuffConfig(TargetBuffConfig.BULLET_OIL_GUN);
                double duration = targetBuffConfigItem.getDuration().get(level) / 1000.0;
                double slowPercentage = targetBuffConfigItem.getEffects().get(level).get(0).getValue();
                SlowEffect slowEffect = this.componentFactory.createSlowEffect(duration, slowPercentage);
                List<EffectComponent> effectComponents = Arrays.asList(slowEffect);
                attackComponent.updateAttackStatistic(damage, attackRange, attackSpeed, effectComponents, bulletSpeed, bulletRadius);
                break;
            }
            case GameConfig.ENTITY_ID.GOAT_TOWER: {
                TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) entity.getComponent(TowerAbilityComponent.typeID);
                TowerConfigItem goatDamageConfig = TowerConfig.INS.getTowerConfig(TowerConfig.GOAT);

                TowerStat towerStat = goatDamageConfig.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double buffAttackDamageEffectPercentage = TowerBuffConfig.INS.getTowerBuffConfig(TowerBuffConfig.ATTACK_AURA).getEffects().get(level).get(0).getValue();

                EffectComponent buffAttackDamageEffect = this.componentFactory.createBuffAttackDamageEffect(buffAttackDamageEffectPercentage);
                towerAbilityComponent.reset(attackRange, buffAttackDamageEffect);
                break;
            }
            case GameConfig.ENTITY_ID.SNAKE_TOWER: {
                TowerAbilityComponent towerAbilityComponent = (TowerAbilityComponent) entity.getComponent(TowerAbilityComponent.typeID);
                TowerConfigItem snakeAttackSpeedConfig = TowerConfig.INS.getTowerConfig(TowerConfig.SNAKE);;

                TowerStat towerStat = snakeAttackSpeedConfig.getStat().get(level);
                double attackRange = towerStat.getRange() * GameConfig.TILE_WIDTH;
                double buffAttackSpeedEffectPercentage = TowerBuffConfig.INS.getTowerBuffConfig(TowerBuffConfig.ATTACK_SPEED_AURA).getEffects().get(level).get(0).getValue();

                EffectComponent buffAttackSpeedEffect = this.componentFactory.createBuffAttackSpeedEffect(buffAttackSpeedEffectPercentage);
                towerAbilityComponent.reset(attackRange, buffAttackSpeedEffect);
                break;
            }
        }
    }

    public void handleDestroyTower(long entityId) throws Exception {
        EntityECS entity = this.entityManager.getEntity(entityId);
        this.entityManager.remove(entity);
    }

    public void handleTowerChangeTargetStrategy(int entityId, int strategyId) throws Exception {
        EntityECS entity = this.entityManager.getEntity(entityId);
        AttackComponent attackComponent = (AttackComponent) entity.getComponent(AttackComponent.typeID);
        attackComponent.setTargetStrategy(strategyId);
    }

    public void debug() {
//        List<EntityECS> monsterList = this.entityManager.getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID));
//        if (GameConfig.DEBUG) System.out.println("MonsterSize=" + monsterList.size());
//
//        List<EntityECS> towerList = this.entityManager.getEntitiesHasComponents(Arrays.asList(TowerInfoComponent.typeID));
//        if (GameConfig.DEBUG) System.out.println("TowerSize=" + towerList.size());
//
//        System.out.println("Player1 Hp= " + this.player1HP + " Player1 Energy=" + this.player1energy);
//        System.out.println("Player2 Hp= " + this.player2HP + " Player2 Energy=" + this.player2energy);
    }

    public BattleMap getBattleMapByEntityMode(EntityMode mode) {
        if (mode == EntityMode.PLAYER) return this.player1BattleMap;
        else return this.player2BattleMap;
    }


    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public EntityFactory getEntityFactory() {
        return this.entityFactory;
    }

    public ComponentFactory getComponentFactory() {
        return this.componentFactory;
    }

    public ComponentManager getComponentManager() {
        return this.componentManager;
    }

    public BattleMap getBattleMapByPlayerId(int playerId) {
        return this.battleMapListByPlayerId.get(playerId);
    }

    public HashMap<Integer, BattleMap> getBattleMapListByPlayerId() {
        return battleMapListByPlayerId;
    }

    public List<List<Integer>> getMonsterWave() {
        return monsterWave;
    }

    public int getWaveAmount() {
        return waveAmount;
    }

    public EntityMode getEntityModeByPlayerID(int id) {
        return this.entityModeByPlayerID.get(id);
    }

    public int getPlayer1HP() {
        return player1HP;
    }

    public int getPlayer2HP() {
        return player2HP;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public void setNextWaveTime(long nextWaveTime) {
        this.nextWaveTime = nextWaveTime;
    }

    public int getPlayer1energy() {
        return player1energy;
    }

    public int getPlayer2energy() {
        return player2energy;
    }

    public void setBattleMapListByPlayerId(HashMap<Integer, BattleMap> battleMapListByPlayerId) {
        this.battleMapListByPlayerId = battleMapListByPlayerId;
    }
}
