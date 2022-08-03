package cmd.send.battle.opponent;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseOppentPutTower extends BaseMsg {
    private int towerId;
    private int towerLevel;
    private Point tilePos;

    public ResponseOppentPutTower(short _error, int towerId, int towerLevel, Point tilePos) {
        super(CmdDefine.OPPONENT_PUT_TOWER, _error);
        this.towerId = towerId;
        this.towerLevel = towerLevel;
        this.tilePos = tilePos;
        createData();
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(towerId);
        bf.putInt(towerLevel);
        bf.putInt(tilePos.x);
        bf.putInt(tilePos.y);
        return packBuffer(bf);
    }
}
