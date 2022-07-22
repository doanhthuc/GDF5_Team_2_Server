package battle.common;

import battle.common.Rect;
import battle.entity.EntityECS;

public class QuadTreeData {
    public Rect pRect;
    public EntityECS entity;

    public QuadTreeData(Rect pRect, EntityECS entity) {
        this.pRect = pRect;
        this.entity = entity;
    }

    public Rect getpRect() {
        return pRect;
    }

    public void setpRect(Rect pRect) {
        this.pRect = pRect;
    }

    public EntityECS getEntity() {
        return entity;
    }

    public void setEntity(EntityECS entity) {
        this.entity = entity;
    }
}
