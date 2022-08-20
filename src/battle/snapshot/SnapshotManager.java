package battle.snapshot;

import battle.Battle;
import battle.common.UUIDGeneratorECS;
import battle.component.common.Component;
import battle.component.common.PathComponent;
import battle.component.common.PositionComponent;
import battle.component.common.VelocityComponent;
import battle.component.effect.SlowEffect;
import battle.component.info.LifeComponent;
import battle.config.GameConfig;
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(5012 * 20);
        Byte Error = 0;
        byteBuffer.put(Error);
        System.out.println("tickSendSnapShot = " + battle.getTickManager().getCurrentTick());
        this.createMonsterSnapShot(byteBuffer);
        this.createBattleInfoSnapShot(byteBuffer);
        return byteBuffer;
    }

    public ByteBuffer createMonsterSnapShot(ByteBuffer byteBuffer) {
        byteBuffer.putInt(abilitySystem.getEntityStore().size());
        for (Map.Entry<Long, EntityECS> entry : abilitySystem.getEntityStore().entrySet()) {
            EntityECS entity = entry.getValue();
            entity.createSnapshot(byteBuffer);
            int sizeComponent = 0;
            for (int component : GameConfig.GROUP_ID.MONSTER_SNAPSHOT) {
                if (entity._hasComponent(component)) sizeComponent++;
            }
            byteBuffer.putInt(sizeComponent);

            for (Map.Entry<Integer, Component> componentEntry : entity.getComponents().entrySet()) {
                int typeID = componentEntry.getValue().getTypeID();
                if (!GameConfig.GROUP_ID.MONSTER_SNAPSHOT.contains(typeID)) continue;
                componentEntry.getValue().createData(byteBuffer);
            }
        }
        return byteBuffer;
    }

    public void createBattleInfoSnapShot(ByteBuffer byteBuffer) {
        byteBuffer.putInt(battle.player1HP);
        byteBuffer.putInt(battle.player2HP);
        UUIDGeneratorECS uuid = battle.getUuidGeneratorECS();
        byteBuffer.putInt(battle.getTickManager().getCurrentTick());
        byteBuffer.putLong(uuid.getPlayerMonsterEntityId());
        byteBuffer.putLong(uuid.getOpponentMonsterEntityId());
        byteBuffer.putLong(uuid.getPlayerStartEntityID());
    }

    public void sendSnapshot(ByteBuffer snapshot) {
        PlayerInfo playerInfo1 = battle.getPlayerInfo1();
        PlayerInfo playerInfo2 = battle.getPlayerInfo2();
        if (FresherExtension.checkUserOnline(playerInfo1.getId())) {
            User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo1.getId());
            ExtensionUtility.getExtension().send(new ResponseSnapshot(snapshot), user);
        }

        if (FresherExtension.checkUserOnline(playerInfo2.getId())) {
            User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo2.getId());
            ExtensionUtility.getExtension().send(new ResponseSnapshot(snapshot), user);
        }
    }

}
