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
    public BattleMap playerBattleMap;
    private HashMap<Integer, BattleMap> battleMapListByPlayerId;
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
    List<Point>[][] playerShortestPath;
    public Battle() throws Exception {
        this.entityManager = EntityManager.getInstance();
        this.attackSystem = new AttackSystem();
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
        this.initMap();
    }

    public Battle(int userId1, int userId2, BattleMap battleMap1, BattleMap battleMap2) throws Exception {
        this.battleMapListByPlayerId = new HashMap<>();
        this.battleMapListByPlayerId.put(userId1, battleMap1);
        this.battleMapListByPlayerId.put(userId2, battleMap2);
        this.entityManager = EntityManager.getInstance();
        //this._initTower();
        this.attackSystem = new AttackSystem();
    }


    public void _initTower() throws Exception {
        EntityFactory.getInstance().createCannonOwlTower(new Point(1, 3), EntityMode.PLAYER);
        EntityFactory.getInstance().createSwordManMonster(new Point(35, 77*3),EntityMode.PLAYER);
    }

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
    }

    public void initMap() {
        this.playerBattleMap = new BattleMap();
        this.playerBattleMap.show();
        this.playerShortestPath = FindPathUtils.findShortestPathForEachTile(playerBattleMap.map);
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
