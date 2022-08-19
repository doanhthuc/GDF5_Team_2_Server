package battle.snapshot;

import battle.Battle;
import battle.component.common.Component;
import battle.entity.EntityECS;
import battle.manager.EntityManager;
import battle.system.AbilitySystem;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.util.ExtensionUtility;
import cmd.send.battle.player.ResponseRequestPutTower;
import cmd.send.battle.snapshot.ResponseSnapshot;
import extension.FresherExtension;
import model.PlayerInfo;
import org.assertj.core.data.MapEntry;
import service.BattleHandler;

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

    public ByteBuffer createSnapshot() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BitZeroServer.getInstance().getConfigurator().getCoreSettings().maxPacketBufferSize - 4);

        byteBuffer.putInt(abilitySystem.getEntityStore().size());

        for (Map.Entry<Long, EntityECS> entry : abilitySystem.getEntityStore().entrySet()) {
            EntityECS entity = entry.getValue();
            entity.createSnapshot(byteBuffer);
            byteBuffer.putInt(entity.getComponents().size());
            for (Map.Entry<Integer, Component> componentEntry : entity.getComponents().entrySet()) {
                componentEntry.getValue().createSnapshot(byteBuffer);
            }
        }

        return byteBuffer;
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
