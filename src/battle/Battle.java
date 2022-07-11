package battle;

import battle.Common.Point;
import battle.Config.GameConfig;
import battle.Factory.EntityFactory;
import battle.Manager.EntityManager;
import battle.System.AttackSystem;
import battle.System.CollisionSystem;
import bitzero.server.BitZeroServer;

import java.util.concurrent.TimeUnit;

public class Battle {
    public BattleMap battleMap;
    public AttackSystem attackSystem;
    public CollisionSystem collisionSystem;
    public EntityManager entityManager;

    public Battle() {
        this.entityManager = EntityManager.getInstance();
        this._initTower();
        this.attackSystem = new AttackSystem();
        BitZeroServer.getInstance().getTaskScheduler().scheduleAtFixedRate(this.attackSystem,0,100, TimeUnit.MILLISECONDS);
    }

    public void _initTower() {
        EntityFactory.getInstance().createCannonOwlTower(new Point(1, 3));
        EntityFactory.getInstance().createSwordManMonster(new Point(35, 77*3), "");
    }
}
