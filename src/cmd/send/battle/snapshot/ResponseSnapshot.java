package cmd.send.battle.snapshot;

import bitzero.server.BitZeroServer;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ResponseSnapshot extends BaseMsg {
    private ByteBuffer snapshotData;

    public ResponseSnapshot(ByteBuffer snapshotData, int player1HP, int player2HP, int currentTick) {
        super(CmdDefine.SEND_SNAPSHOT);
        this.snapshotData = snapshotData;

//        this.snapshotData.putInt(player1HP);
//        this.snapshotData.putInt(player2HP);
//        this.snapshotData.putInt(currentTick);
    }

    @Override
    public byte[] createData() {
        return packBuffer(snapshotData);
    }
}
