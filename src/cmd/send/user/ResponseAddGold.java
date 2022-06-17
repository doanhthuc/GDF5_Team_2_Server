package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 */
public class ResponseAddGold extends BaseMsg {
    private int userGold;
    public ResponseAddGold(short error, int userGold) {
        super(CmdDefine.ADD_USER_GOLD, error);
        this.userGold=userGold;
    }
    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.userGold);
        return packBuffer(bf);
    }

    public int getUserGold()
    {
        return this.userGold;
    }
}
