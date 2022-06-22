package cmd.send.user;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.PlayerInfo;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 */
public class ResponseAddGem extends BaseMsg {
    private int userGem;

    public ResponseAddGem(short error, int userGem) {
        super(CmdDefine.ADD_USER_GOLD, error);
        this.userGem = userGem;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.userGem);
        return packBuffer(bf);
    }

    public int getUserGem() {
        return this.userGem;
    }
}

