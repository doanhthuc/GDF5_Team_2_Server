package cmd.send.battle.player;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseRequestPutTrap extends BaseMsg {
    private final short _error;
    private final Point tilePos;
    public ResponseRequestPutTrap(short _error, Point tilePos) {
        super(CmdDefine.PUT_TRAP);
        this._error = _error;
        this.tilePos = tilePos;
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(tilePos.x);
        bf.putInt(tilePos.y);
        System.out.println("dsjghksdvoudgasovuasovousdgfo");
        return packBuffer(bf);
    }
}
