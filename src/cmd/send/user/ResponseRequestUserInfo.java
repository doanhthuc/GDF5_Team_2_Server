package cmd.send.user;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.PlayerInfo;

import java.nio.ByteBuffer;

public class ResponseRequestUserInfo extends BaseMsg {
    public PlayerInfo info;
    public short error;

    public ResponseRequestUserInfo(short _error, PlayerInfo _info) {
        super(CmdDefine.GET_USER_INFO);
        info = _info;
        error = _error;
    }
    public ResponseRequestUserInfo(short _error) {
        super(CmdDefine.GET_USER_INFO);
        error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt((int)info.getId());
        putStr(bf, info.getUserName());
        bf.putInt(info.getGold());
        bf.putInt(info.getGem());
        bf.putInt(info.getTrophy());
        bf.putLong(System.currentTimeMillis());
        return packBuffer(bf);
    }
}