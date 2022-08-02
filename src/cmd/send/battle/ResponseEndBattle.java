package cmd.send.battle;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseEndBattle extends BaseMsg {
    private final short _error;
    private String battleResult;

    public ResponseEndBattle(short _error, String battleResult) {
        super(CmdDefine.END_BATTLE, _error);
        this.battleResult = battleResult;
        this._error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        putStr(bf, this.battleResult);
        return packBuffer(bf);
    }
}
