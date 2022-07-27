package cmd.send.battle.opponent;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseOpponentPutTrap extends BaseMsg {
    private final short _error;
    private final Point tilePos;
    public ResponseOpponentPutTrap(short _error, Point tilePos) {
        super(CmdDefine.OPPONENT_PUT_TRAP);
        this._error = _error;
        this.tilePos = tilePos;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(tilePos.x);
        bf.putInt(tilePos.y);
        return packBuffer(bf);
    }
}
