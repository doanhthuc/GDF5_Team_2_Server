package battle.snapshot;

import battle.Battle;
import battle.common.UUIDGeneratorECS;
import battle.component.common.Component;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.info.LifeComponent;
import battle.entity.EntityECS;
import battle.manager.EntityManager;
import battle.system.AbilitySystem;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.send.battle.snapshot.ResponseSnapshot;
import extension.FresherExtension;
import model.PlayerInfo;

import java.nio.ByteBuffer;
import java.util.Map;

public class SnapshotManager {
    private EntityManager entityManager;
    private AbilitySystem abilitySystem;
    private Battle battle;

    public SnapshotManager(Battle battle) {
        this.entityManager = battle.getEntityManager();
        this.abilitySystem = battle.abilitySystem;
        this.battle = battle;
    }

    public ByteBuffer createAllSnapshot() {
        // should increase size buff if snapshot is big
        ByteBuffer byteBuffer = ByteBuffer.allocate(BitZeroServer.getInstance().getConfigurator().getCoreSettings().maxPacketBufferSize);
        Byte Error = 0;
        byteBuffer.put(Error);
        System.out.println("tickSendSnapShot= " + battle.getTickManager().getCurrentTick());
        this.createMonsterSnapShot(byteBuffer);
        return byteBuffer;
    }

    public ByteBuffer createMonsterSnapShot(ByteBuffer byteBuffer) {
        byteBuffer.putInt(abilitySystem.getEntityStore().size());
        for (Map.Entry<Long, EntityECS> entry : abilitySystem.getEntityStore().entrySet()) {
            EntityECS entity = entry.getValue();
            entity.createSnapshot(byteBuffer);

            int sizeComponent = 0;
            if (entity._hasComponent(PathComponent.typeID)) sizeComponent++;
            if (entity._hasComponent(PositionComponent.typeID)) sizeComponent++;
            if (entity._hasComponent(LifeComponent.typeID)) sizeComponent++;
            if (entity._hasComponent(VelocityComponent.typeID)) sizeComponent++;
            byteBuffer.putInt(sizeComponent);

            for (Map.Entry<Integer, Component> componentEntry : entity.getComponents().entrySet()) {
                int typeID = componentEntry.getValue().getTypeID();
                if (!(typeID == PathComponent.typeID || typeID == PositionComponent.typeID || typeID == LifeComponent.typeID || typeID == VelocityComponent.typeID))
                    continue;
                componentEntry.getValue().createData(byteBuffer);
            }
        }
        return byteBuffer;
    }

    public void sendSnapshot(ByteBuffer snapshot) {
        PlayerInfo playerInfo1 = battle.getPlayerInfo1();
        PlayerInfo playerInfo2 = battle.getPlayerInfo2();

        UUIDGeneratorECS uuid = battle.getUuidGeneratorECS();
        if (FresherExtension.checkUserOnline(playerInfo1.getId())) {
            User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo1.getId());
            ExtensionUtility.getExtension().send(new ResponseSnapshot(snapshot, battle.player1HP, battle.player2HP, battle.getTickManager().getCurrentTick()
                    , uuid.getPlayerMonsterEntityId(), uuid.getOpponentMonsterEntityId()), user);
        }

        if (FresherExtension.checkUserOnline(playerInfo2.getId())) {
            User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo2.getId());
            ExtensionUtility.getExtension().send(new ResponseSnapshot(snapshot, battle.player2HP, battle.player1HP, battle.getTickManager().getCurrentTick(),
                    uuid.getOpponentMonsterEntityId(), uuid.getPlayerMonsterEntityId()), user);
        }
    }
}
