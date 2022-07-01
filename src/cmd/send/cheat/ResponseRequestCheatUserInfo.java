package cmd.send.cheat;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.PlayerInfo;

import java.nio.ByteBuffer;

public class ResponseRequestCheatUserInfo extends BaseMsg {
    public PlayerInfo info;
    public short error;

    public ResponseRequestCheatUserInfo(short _error, PlayerInfo _info) {
        super(CmdDefine.CHEAT_USER_INFO);
        info = _info;
        error = _error;
    }
    public ResponseRequestCheatUserInfo(short _error) {
        super(CmdDefine.CHEAT_USER_INFO);
        error = _error;
    }
    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(info.getGold());
        bf.putInt(info.getGem());
        bf.putInt(info.getTrophy());
        return packBuffer(bf);
    }
}