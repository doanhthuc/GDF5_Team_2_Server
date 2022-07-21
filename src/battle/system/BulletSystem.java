package battle.system;

import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.Arrays;
import java.util.List;

public class BulletSystem extends SystemECS implements Runnable {
    public int id = GameConfig.SYSTEM_ID.BULLET;
    public String name = "BulletSystem";

    public BulletSystem() {
        super(GameConfig.SYSTEM_ID.BULLET);
    }

    @Override
    public void run() {
        this.tick = this.getElapseTime();
        List<Integer> componentIdList = Arrays.asList(GameConfig.COMPONENT_ID.VELOCITY, GameConfig.COMPONENT_ID.POSITION, GameConfig.COMPONENT_ID.BULLET_INFO);
        List<EntityECS> bulletList = EntityManager.getInstance().getEntitiesHasComponents(componentIdList);

        for (EntityECS bullet : bulletList) {
            PositionComponent bulletPos = (PositionComponent) bullet.getComponent(GameConfig.COMPONENT_ID.POSITION);
            VelocityComponent bulletVelocity = (VelocityComponent) bullet.getComponent(GameConfig.COMPONENT_ID.VELOCITY);

        }
    }
}
