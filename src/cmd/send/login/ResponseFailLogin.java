package cmd.send.login;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseFailLogin extends BaseMsg {
    public short error;
    public String message;
    public ResponseFailLogin(short _error, String message) {
        super(CmdDefine.CUSTOM_LOGIN, _error);
        this.error = _error;
        this.message = message;
    }
    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(this.error);
        bf.put(this.message.getBytes());
        return packBuffer(bf);
    }
}
