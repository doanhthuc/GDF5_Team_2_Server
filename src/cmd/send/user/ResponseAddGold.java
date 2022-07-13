package cmd.send.user;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 */
public class ResponseAddGold extends BaseMsg {
    private int gold;
    private short error;
    public ResponseAddGold(short error, int gold) {
        super(CmdDefine.ADD_USER_GOLD, error);
        this.gold = gold;
        this.error=error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.gold);
        return packBuffer(bf);
    }

    public int getGold() {
        return this.gold;
    }
}

