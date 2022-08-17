package battle.system;

import battle.Battle;
import battle.component.common.*;
import battle.component.info.BulletInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BulletSystem extends SystemECS {
    private static final String SYSTEM_NAME = "BulletSystem";

    public BulletSystem(long id) {
        super(GameConfig.SYSTEM_ID.BULLET, SYSTEM_NAME, id);
    }

    @Override
    public void run(Battle battle) {
        this.tick = this.getElapseTime();

        for (Map.Entry<Long, EntityECS>  mapElement : this.getEntityStore().entrySet()) {
            EntityECS bullet = mapElement.getValue();
            PositionComponent bulletPos = (PositionComponent) bullet.getComponent(PositionComponent.typeID);
            VelocityComponent bulletVelocity = (VelocityComponent) bullet.getComponent(VelocityComponent.typeID);
            PathComponent pathComponent = (PathComponent) bullet.getComponent(PathComponent.typeID);
            // if Frog
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
