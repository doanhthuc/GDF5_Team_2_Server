package cmd.send.battle.player;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseRequestDestroyTower extends BaseMsg {
    private final short _error;
    private final Point tilePos;

    public ResponseRequestDestroyTower(short _error, Point tilePos) {
        super(CmdDefine.DESTROY_TOWER);
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
