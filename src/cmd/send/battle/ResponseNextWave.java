package cmd.send.battle;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.util.List;
import java.nio.ByteBuffer;

public class ResponseNextWave extends BaseMsg {
    private final short _error;
    private int currentTick;
    private List<Integer> currentWave;

    public ResponseNextWave(short _error, List currentWave, int currentTick) {
        super(CmdDefine.NEXT_WAVE, _error);
        this.currentWave = currentWave;
        this.currentTick = currentTick;
        this._error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(currentTick);
        bf.putInt(currentWave.size());
        for (int i = 0; i < this.currentWave.size(); i++) {
            bf.putInt(this.currentWave.get(i));
        }
        return packBuffer(bf);
    }
}
