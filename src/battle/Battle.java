package battle;

import battle.common.Point;
import battle.factory.EntityFactory;
import battle.manager.EntityManager;
import battle.system.AttackSystem;
import battle.system.CollisionSystem;

import java.util.HashMap;

public class Battle {
    public BattleMap battleMap;
    private HashMap<Integer, BattleMap> battleMapListByPlayerId;


    public AttackSystem attackSystem;
    public CollisionSystem collisionSystem;
    public EntityManager entityManager;

    public Battle() {
        this.entityManager = EntityManager.getInstance();
        this._initTower();
        this.attackSystem = new AttackSystem();
    }

    public Battle(int userId1, int userId2, BattleMap battleMap1, BattleMap battleMap2) {
        this.battleMapListByPlayerId = new HashMap<>();
        this.battleMapListByPlayerId.put(userId1, battleMap1);
        this.battleMapListByPlayerId.put(userId2, battleMap2);
        this.entityManager = EntityManager.getInstance();
        this._initTower();
        this.attackSystem = new AttackSystem();
    }

    public Battle(BattleMap battleMap) {
        this.entityManager = EntityManager.getInstance();
        this._initTower();
        this.attackSystem = new AttackSystem();
        this.battleMap = battleMap;
    }

    public void _initTower() {
        EntityFactory.getInstance().createCannonOwlTower(new Point(1, 3));
        EntityFactory.getInstance().createSwordManMonster(new Point(35, 77*3), "");
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
