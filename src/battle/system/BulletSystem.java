package battle.system;

import battle.Battle;
import battle.component.common.*;
import battle.component.info.BulletInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.Arrays;
import java.util.List;

public class BulletSystem extends SystemECS {
    private static final String SYSTEM_NAME = "BulletSystem";

    public BulletSystem() {
        super(GameConfig.SYSTEM_ID.BULLET, SYSTEM_NAME);
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

            if (bulletVelocity.getDynamicPosition(battle) == null && bulletVelocity.hasDynamicEntityId()) {
                battle.getEntityManager().destroy(bullet);
                continue;
            }

            if (bulletVelocity.getDynamicPosition(battle) != null) {
                if (Math.abs(bulletVelocity.getDynamicPosition(battle).getX() - bulletPos.getX()) <= 10
                        || Math.abs(bulletVelocity.getDynamicPosition(battle).getY() - bulletPos.getY()) <= 10) {
                    CollisionComponent collisionComponent = (CollisionComponent) bullet.getComponent(CollisionComponent.typeID);
                    if (collisionComponent != null) {
                        collisionComponent.setWidth(collisionComponent.getOriginWidth());
                        collisionComponent.setHeight(collisionComponent.getOriginHeight());
                    }
                }
            }
        }
    }

    @Override
    public boolean checkEntityCondition(EntityECS entity, Component component) {
        return component.getTypeID() == BulletInfoComponent.typeID;
    }
}
