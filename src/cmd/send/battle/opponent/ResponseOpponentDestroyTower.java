package cmd.send.battle.opponent;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseOpponentDestroyTower extends BaseMsg {
    private final short _error;
    private final Point tilePos;
    private int tickNumber;

    public ResponseOpponentDestroyTower(short _error, Point tilePos, int tickNumber) {
        super(CmdDefine.OPPONENT_DESTROY_TOWER);
        this._error = _error;
        this.tilePos = tilePos;
        this.tickNumber = tickNumber;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(tilePos.x);
        bf.putInt(tilePos.y);
        bf.putInt(tickNumber);
        return packBuffer(bf);
    }
}
