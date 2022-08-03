package battle;

import battle.common.*;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.info.MonsterInfoComponent;
import battle.component.info.TowerInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.factory.ComponentFactory;
import battle.factory.EntityFactory;
import battle.manager.ComponentManager;
import battle.manager.EntityManager;
import battle.newMap.BattleMapObject;
import battle.newMap.Tower;
import battle.pool.ComponentPool;
import battle.pool.EntityPool;
import battle.system.*;

import javax.swing.plaf.BorderUIResource;
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
    private int player1HP = GameConfig.PLAYER_HP;
    private int player2HP = GameConfig.PLAYER_HP;
    private int player1energy = GameConfig.PLAYER_ENERGY;
    private int player2energy = GameConfig.PLAYER_ENERGY;
    //DDojc va luu


    public Battle(int userId1, int userId2) {
        this.initPoolAndManager();
        this.initSystem();
        this.initMap(userId1, userId2);
        this.initMonsterWave();
    }

    //    public Battle(int userId1, int userId2, BattleMap battleMap1, BattleMap battleMap2) throws Exception {
//        this.battleMapListByPlayerId = new HashMap<>();
//        this.battleMapListByPlayerId.put(userId1, battleMap1);
//        this.battleMapListByPlayerId.put(userId2, battleMap2);
//
//        this.entityManager = EntityManager.getInstance();
//        //this._initTower();
//        this.attackSystem = new AttackSystem();
//    }
//  INIT
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

    public void initMap(int userId1, int userId2) {
        this.player1BattleMap = new BattleMap();
        this.player1ShortestPath = FindPathUtils.findShortestPathForEachTile(player1BattleMap.map);

        this.player2BattleMap = new BattleMap();
        this.player2ShortestPath = FindPathUtils.findShortestPathForEachTile(player2BattleMap.map);

        this.battleMapListByPlayerId.put(userId1, this.player1BattleMap);
        this.battleMapListByPlayerId.put(userId2, this.player2BattleMap);

        this.entityModeByPlayerID.put(userId1, EntityMode.PLAYER);
        this.entityModeByPlayerID.put(userId2, EntityMode.OPPONENT);
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
        this.currentWave = -1;
    }

//Update

    public void updateSystem() throws Exception {
        if (GameConfig.DEBUG) this.debug();
        resetSystem.run(this);
        abilitySystem.run(this);
        attackSystem.run(this);
        bulletSystem.run(this);
        pathMonsterSystem.run(this);
        collisionSystem.run(this);
        effectSystem.run(this);
        lifeSystem.run(this);
        movementSystem.run(this);
        monsterSystem.run(this);
        spellSystem.run(this);
    }

    public void updateMonsterWave() throws Exception {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= this.nextWaveTime) {
            this.currentWave += 1;
            this.nextWaveTime += 20000;
        }
        if (this.currentWave < 0) return;
        List<Integer> currentWaveList = this.monsterWave.get(this.currentWave);
        //System.out.println(this.nextBornMonsterTime - currentTime);
        if (currentTime >= this.nextBornMonsterTime && (currentWaveList.size() > 0)) {
            this.bornMonsterByMonsterID(this.monsterWave.get(this.currentWave).get(currentWaveList.size() - 1), EntityMode.PLAYER);
            this.bornMonsterByMonsterID(this.monsterWave.get(this.currentWave).get(currentWaveList.size() - 1), EntityMode.OPPONENT);
            this.monsterWave.get(currentWave).remove(currentWaveList.size() - 1);
            this.nextBornMonsterTime = currentTime + 1000;
        }
    }

    //Other Function

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


    public List<List<Integer>> createNewMonsterWave() {
        List<List<Integer>> monsterWave = new ArrayList<>(this.waveAmount);
        for (int waveIdx = 1; waveIdx <= this.waveAmount; waveIdx++) {
            List<Integer> wave = new ArrayList<>();
            int swordManAmount, batAmount, ninjaAmount, assassinAmount;
            int monsterAmountInWave = Math.min(5 + waveIdx, 15);
            swordManAmount = (int) Math.floor(Math.random() * monsterAmountInWave);
            batAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount));
            ninjaAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount - batAmount));
            assassinAmount = (int) Math.floor(Math.random() * (monsterAmountInWave - swordManAmount - batAmount - ninjaAmount));
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
        switch (towerID) {
            case GameConfig.ENTITY_ID.CANNON_TOWER:
                this.entityFactory.createCannonOwlTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.FROG_TOWER:
                this.entityFactory.createFrogTower(new Point(tilePosX, tilePosY), mode);
                System.out.println("FROG");
                break;
            case GameConfig.ENTITY_ID.WIZARD_TOWER:
                this.entityFactory.createWizardTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.BEAR_TOWER:
                this.entityFactory.createIceGunPolarBearTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.BUNNY_TOWER:
                this.entityFactory.createBunnyOilGunTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.SNAKE_TOWER:
                this.entityFactory.createSnakeAttackSpeedTower(new Point(tilePosX, tilePosY), mode);
                break;
            case GameConfig.ENTITY_ID.GOAT_TOWER:
                this.entityFactory.createGoatAttackDamageTower(new Point(tilePosX, tilePosY), mode);
                break;
        }
        this.updateMap(towerID, tilePosX, tilePosY, mode);
        this.handlerPutTower(mode);
    }

    public void updateMap(int towerId, int tilePosX, int tilePosY, EntityMode mode) {
        BattleMap battleMap = null;
        if (mode == EntityMode.PLAYER) {
            this.player1BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;
            battleMap = this.player1BattleMap;
        }
        if (mode == EntityMode.OPPONENT) {
            this.player2BattleMap.map[tilePosX][tilePosY] = GameConfig.MAP.TOWER;
            battleMap = this.player2BattleMap;
        }
        BattleMapObject battleMapObject = battleMap.battleMapObject;
        battleMapObject.putTowerIntoMap(new java.awt.Point(tilePosX, tilePosY), towerId);
    }

    public void castSpellByID(int spellID, int pixelPosX, int pixelPosY, EntityMode mode) throws Exception {
        switch (spellID) {
            case GameConfig.ENTITY_ID.FIRE_SPELL:
                //System.out.println(pixelPos.getX()+ " "+pixelPos.getY());
                this.entityFactory.createFireSpell(new Point(pixelPosX, pixelPosY), mode);
                break;
            case GameConfig.ENTITY_ID.FROZEN_SPELL:
                this.entityFactory.createFrozenSpell(new Point(pixelPosX, pixelPosY), mode);
                break;
        }

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

    public void debug() {
        List<EntityECS> monsterList = this.entityManager.getEntitiesHasComponents(Arrays.asList(MonsterInfoComponent.typeID));
        if (GameConfig.DEBUG) System.out.println("MonsterSize=" + monsterList.size());

        List<EntityECS> towerList = this.entityManager.getEntitiesHasComponents(Arrays.asList(TowerInfoComponent.typeID));
        if (GameConfig.DEBUG) System.out.println("TowerSize=" + towerList.size());

        System.out.println("Player1 Hp= " + this.player1HP + " Player1 Energy=" + this.player1energy);
        System.out.println("Player2 Hp= " + this.player2HP + " Player2 Energy=" + this.player2energy);
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

    public void setBattleMapListByPlayerId(HashMap<Integer, BattleMap> battleMapListByPlayerId) {
        this.battleMapListByPlayerId = battleMapListByPlayerId;
    }
}
