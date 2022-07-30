package battle.system;

import battle.Battle;
import battle.component.common.CollisionComponent;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.Arrays;
import java.util.List;

public class BulletSystem extends SystemECS {
    public int id = GameConfig.SYSTEM_ID.BULLET;
    public String name = "BulletSystem";

    public BulletSystem() {
        super(GameConfig.SYSTEM_ID.BULLET);
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();
        List<Integer> componentIdList = Arrays.asList(GameConfig.COMPONENT_ID.VELOCITY, GameConfig.COMPONENT_ID.POSITION, GameConfig.COMPONENT_ID.BULLET_INFO);
        List<EntityECS> bulletList = battle.getEntityManager().getEntitiesHasComponents(componentIdList);

        for (EntityECS bullet : bulletList) {
            PositionComponent bulletPos = (PositionComponent) bullet.getComponent(PositionComponent.typeID);
            VelocityComponent bulletVelocity = (VelocityComponent) bullet.getComponent(VelocityComponent.typeID);
            PathComponent pathComponent = (PathComponent) bullet.getComponent(PathComponent.typeID);

            if (pathComponent != null) {
                if (pathComponent.getCurrentPathIDx() == pathComponent.getPath().size() - 2) {
                    battle.getEntityManager().destroy(bullet);
                }
                continue;
            }

            if (bulletVelocity.getDynamicPosition() == null) continue;

            if (!bulletVelocity.getDynamicPosition().getActive()) {
                bulletVelocity.setDynamicPosition(null);
                battle.getEntityManager().destroy(bullet);
                continue;
            }

            if (Math.abs(bulletVelocity.getDynamicPosition().getX() - bulletPos.getX()) <= 3
                    && Math.abs(bulletVelocity.getDynamicPosition().getY() - bulletPos.getY()) <= 3) {
                CollisionComponent collisionComponent = (CollisionComponent) bullet.getComponent(GameConfig.COMPONENT_ID.COLLISION);
                if (collisionComponent != null) {
                    collisionComponent.setWidth(collisionComponent.getOriginWidth());
                    collisionComponent.setHeight(collisionComponent.getOriginHeight());
                }
            }
        }
    }
}
