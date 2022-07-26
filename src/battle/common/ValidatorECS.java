package battle.common;

import battle.config.GameConfig;
import battle.entity.EntityECS;

import java.util.List;

public class ValidatorECS {
    public static boolean isEntityInGroupId(EntityECS entity, List<Integer> groupIds) {
        int typeId = entity.getTypeID();

        for (int id: groupIds) {
            if (typeId == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEntityInGroupId(int typeId, List<Integer> groupIds) {
        for (int id: groupIds) {
            if (typeId == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEntityIdEqualTypeId(EntityECS entity, int typeId) {
        return entity.getTypeID() == typeId;
    }

    public static boolean isEntityIdEqualTypeId(int entityTypeId, int typeId) {
        return entityTypeId == typeId;
    }
}
