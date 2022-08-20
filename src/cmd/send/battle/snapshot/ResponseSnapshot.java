package cmd.send.battle.snapshot;

import bitzero.server.BitZeroServer;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ResponseSnapshot extends BaseMsg {
    private ByteBuffer snapshotData;

    public ResponseSnapshot(ByteBuffer snapshotData) {
        super(CmdDefine.SEND_SNAPSHOT);
        this.snapshotData = snapshotData;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        snapshotData.rewind();
        while (snapshotData.hasRemaining()) {
            byte a = snapshotData.get();
            bf.put(a);
        }
        return packBuffer(bf);
    }
}
