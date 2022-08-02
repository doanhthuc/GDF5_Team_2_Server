package battle;

import battle.common.EntityMode;
import battle.common.FindPathUtils;
import battle.common.Point;
import battle.common.UUIDGeneratorECS;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;
import battle.factory.EntityFactory;
import battle.manager.ComponentManager;
import battle.manager.EntityManager;
import battle.pool.ComponentPool;
import battle.pool.EntityPool;
import battle.system.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Battle {
    private HashMap<Integer, BattleMap> battleMapListByPlayerId = new HashMap<>();
    private ComponentPool componentPool;
    private EntityPool entityPool;
    private EntityManager entityManager;
    private ComponentManager componentManager;
    private ComponentFactory componentFactory;
    private EntityFactory entityFactory;

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

    public BattleMap player1BattleMap;
    public List<Point>[][] player1ShortestPath;

    public BattleMap player2BattleMap;
    public List<Point>[][] player2ShortestPath;

    private int waveAmount = 20;
    private List<List<Integer> > monsterWave;



    public Battle(int userId1, int userId2) {
        this.entityPool = new EntityPool();
        this.componentPool = new ComponentPool();
        this.entityManager = new EntityManager();
        this.componentManager = new ComponentManager();
        this.componentFactory = new ComponentFactory(this.componentManager, this.componentPool);
        this.entityFactory = new EntityFactory(this.entityManager, this.componentFactory, this.entityPool);
        //InitSystem
        {
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

        //initMap
        this.initMap(userId1, userId2);
        this.monsterWave = this.createNewMonsterWave();
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


    public void updateSystem() throws Exception {
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

    public void updateMonsterWave(){

    }
    public void initMap(int userId1, int userId2) {
        this.player1BattleMap = new BattleMap();
        //this.player1BattleMap.show();
        this.player1ShortestPath = FindPathUtils.findShortestPathForEachTile(player1BattleMap.map);

        this.player2BattleMap = new BattleMap();
        //this.player2BattleMap.show();
        this.player2ShortestPath = FindPathUtils.findShortestPathForEachTile(player2BattleMap.map);

        this.battleMapListByPlayerId.put(userId1, this.player1BattleMap);
        this.battleMapListByPlayerId.put(userId2, this.player2BattleMap);
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
    public void setBattleMapListByPlayerId(HashMap<Integer, BattleMap> battleMapListByPlayerId) {
        this.battleMapListByPlayerId = battleMapListByPlayerId;
    }
}
