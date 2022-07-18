package battle.system;

import battle.common.Utils;
import battle.component.Component.CollisionComponent;
import battle.component.Component.PositionComponent;
import battle.component.EffectComponent.EffectComponent;
import battle.component.InfoComponent.BulletInfoComponent;
import battle.config.GameConfig;
import battle.entity.EntityECS;
import battle.manager.EntityManager;

import java.util.ArrayList;

public class CollisionSystem extends System {
    int id = GameConfig.SYSTEM_ID.LIFE;
    String name = "CollisionSystem";

    public CollisionSystem() {
        java.lang.System.out.println(this.name);
    }

    @Override
    public void run() {
        this.tick=this.getEclapseTime();
        ArrayList<Integer> typeIDs = new ArrayList<>();
        typeIDs.add(GameConfig.COMPONENT_ID.COLLISION);
        ArrayList<EntityECS> entityList = EntityManager.getInstance().getEntitiesHasComponents(typeIDs);

        for (int i = 0; i < entityList.size() - 1; i++) {
            for (int j = 1; j < entityList.size(); j++) {
                EntityECS entity1 = entityList.get(i);
                EntityECS entity2 = entityList.get(j);
                if (this._isCollide(entity1, entity2)) {
                    if ((Utils.isMonster(entity1) && Utils.isBullet(entity2))
                            || Utils.isBullet(entity1) && Utils.isMonster(entity2)) {
                        EntityECS bullet = Utils.isBullet(entity1) ? entity1 : entity2;
                        EntityECS monster = Utils.isMonster(entity1) ? entity1 : entity2;
                        BulletInfoComponent bulletInfo = (BulletInfoComponent) bullet.getComponent(GameConfig.COMPONENT_ID.BULLET_INFO);
                        for (EffectComponent effectComponent : bulletInfo.getEffects()) {
                            monster.addComponent(effectComponent.clone());
                        }
                    }
                }
            }
        }
    }

    public boolean _isCollide(EntityECS entity1, EntityECS entity2) {
        PositionComponent pos1 = (PositionComponent) entity1.getComponent(GameConfig.COMPONENT_ID.POSITION);
        PositionComponent pos2 = (PositionComponent) entity1.getComponent(GameConfig.COMPONENT_ID.POSITION);
        CollisionComponent collision1 = (CollisionComponent) entity1.getComponent(GameConfig.COMPONENT_ID.COLLISION);
        CollisionComponent collision2 = (CollisionComponent) entity2.getComponent(GameConfig.COMPONENT_ID.COLLISION);
        double w1 = collision1.getWidth(), h1 = collision1.getHeight();
        double w2 = collision2.getWidth(), h2 = collision2.getHeight();
        if ((w1 == 0 && h1 == 0) || (w2 == 0) && (h2 == 0)) return false;

        return this._interSectRect(pos1.getX() - w1 / 2, pos1.getY() - h1 / 2, w1, h1, pos2.getX()- w2 / 2, pos2.getY() - h2 / 2, w2, h2);
    }

    public boolean _interSectRect(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
        if (((x1 + w1 >= x2) && (x1 <= x2 + w2)) && (y1 + h1 >= y2) && (y1 <= y2 + h2)) {
            return true;
        }
        return false;
    }
}
