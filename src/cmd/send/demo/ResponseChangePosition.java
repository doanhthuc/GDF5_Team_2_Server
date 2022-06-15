package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseChangePosition extends BaseMsg {
    private Point pos;
    public ResponseChangePosition(short error, Point pos) {
        super(CmdDefine.MOVE, error);
        this.pos = pos;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.pos.x);
        bf.putInt(this.pos.y);
        return packBuffer(bf);
    }

}
