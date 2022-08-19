package cmd.send.battle.snapshot;

import bitzero.server.BitZeroServer;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ResponseSnapshot extends BaseMsg {
    private ByteBuffer snapshotData;

    public ResponseSnapshot(ByteBuffer snapshotData, int player1HP, int player2HP, int currentTick, long playerEntityId, long opponentEntityId) {
        super(CmdDefine.SEND_SNAPSHOT);
        snapshotData.putInt(player1HP);
        snapshotData.putInt(player2HP);
        snapshotData.putInt(currentTick);
        snapshotData.putLong(playerEntityId);
        snapshotData.putLong(opponentEntityId);
        this.snapshotData = snapshotData;

    }

    @Override
    public byte[] createData() {
        return packBuffer(snapshotData);
    }
}
