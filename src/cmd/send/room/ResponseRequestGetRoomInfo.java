package cmd.send.room;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseRequestGetRoomInfo extends BaseMsg {
    public short error;
    private int userId;

    public ResponseRequestGetRoomInfo(short error) {
        super(CmdDefine.ENTER_ROOM);
        this.error = error;
    }

    public ResponseRequestGetRoomInfo(short _error, int userId) {
        super(CmdDefine.ENTER_ROOM);
        this.userId = userId;
        error = _error;
    }

    public short getError() {
        return error;
    }

    public void setError(short error) {
        this.error = error;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.userId);
        return packBuffer(bf);
    }
}
