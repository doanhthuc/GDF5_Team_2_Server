package cmd.send.battle.player;


import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseRequestPutTower extends BaseMsg {
    private short error;
    private int towerId;
    private int towerLevel;
    private Point tilePos;
    private int tickNumber;

    public ResponseRequestPutTower(short error, int towerId, int towerLevel, Point tilePos, int tickNumber) {
        super(CmdDefine.PUT_TOWER);
        this.error = error;
        this.towerId = towerId;
        this.towerLevel = towerLevel;
        this.tilePos = tilePos;
        this.tickNumber = tickNumber;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(towerId);
        bf.putInt(towerLevel);
        bf.putInt(tilePos.x);
        bf.putInt(tilePos.y);
        bf.putInt(tickNumber);
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

    public int getTowerLevel() {
        return towerLevel;
    }

    public void setTowerLevel(int towerLevel) {
        this.towerLevel = towerLevel;
    }

    public int getTickNumber() {
        return tickNumber;
    }

    public void setTickNumber(int tickNumber) {
        this.tickNumber = tickNumber;
    }
}
