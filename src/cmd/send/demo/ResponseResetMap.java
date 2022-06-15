package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseLoginFail extends BaseMsg {
    public ResponseResetMap(short type) {
        super(CmdDefine.RESET_MAP, type);
    }

    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        return packBuffer(bf);
    }
}
