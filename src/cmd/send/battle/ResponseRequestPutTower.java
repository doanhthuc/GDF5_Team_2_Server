package cmd.send.battle;

import battle.Common.Point;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseRequestPutTower extends BaseMsg {
    private short error;
    private int towerId;
    private Point tilePos;
    private Point pixelPos;

    public ResponseRequestPutTower(short error, int towerId, Point tilePos, Point pixelPos) {
        super(CmdDefine.PUT_TOWER);
        this.error = error;
        this.towerId = towerId;
        this.tilePos = tilePos;
        this.pixelPos = pixelPos;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(towerId);
        bf.putInt((int) tilePos.x);
        bf.putInt((int) tilePos.y);
        bf.putDouble(pixelPos.x);
        bf.putDouble(pixelPos.y);
        return packBuffer(bf);
    }

    public short getError() {
        return error;
    }

    public void setError(short error) {
        this.error = error;
    }

    public int getTowerId() {
        return towerId;
    }

    public void setTowerId(int towerId) {
        this.towerId = towerId;
    }

    public Point getTilePos() {
        return tilePos;
    }

    public void setTilePos(Point tilePos) {
        this.tilePos = tilePos;
    }
}
