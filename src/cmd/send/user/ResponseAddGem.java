package cmd.send.user;

import bitzero.server.exceptions.IErrorCode;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 */
public class ResponseAddGem extends BaseMsg {
    private int gem;
    private short error;
    public ResponseAddGem(short error, int gem) {
        super(CmdDefine.ADD_USER_GEM, error);
        this.gem = gem;
        this.error=error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(this.error);
        bf.putInt(this.gem);
        return packBuffer(bf);
    }

    public int getUserGem() {
        return this.gem;
    }
}

