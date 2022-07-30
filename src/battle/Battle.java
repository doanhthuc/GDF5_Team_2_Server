package battle;

import battle.common.EntityMode;
import battle.common.FindPathUtils;
import battle.common.Point;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;
import battle.system.*;

import java.util.HashMap;
import java.util.List;

public class Battle {
    private HashMap<Integer, BattleMap> battleMapListByPlayerId = new HashMap<>();
    public AttackSystem attackSystem;
    public EntityManager entityManager;
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
    List<Point>[][] player1ShortestPath;

    public BattleMap player2BattleMap;
    List<Point>[][] player2ShortestPath;
    public Battle(int userId1, int userId2)  {
        this.entityManager = EntityManager.getInstance();
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
        this.initMap(userId1,userId2);
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


    public void updateSystem() {
        resetSystem.run();
        abilitySystem.run();
        attackSystem.run();
        bulletSystem.run();
        pathMonsterSystem.run();
        collisionSystem.run();
        effectSystem.run();
        lifeSystem.run();
        movementSystem.run();
        monsterSystem.run();
        spellSystem.run();
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

    public BattleMap getBattleMapByPlayerId(int playerId) {
        return this.battleMapListByPlayerId.get(playerId);
    }

    public HashMap<Integer, BattleMap> getBattleMapListByPlayerId() {
        return battleMapListByPlayerId;
    }

    public void setBattleMapListByPlayerId(HashMap<Integer, BattleMap> battleMapListByPlayerId) {
        this.battleMapListByPlayerId = battleMapListByPlayerId;
    }
}
