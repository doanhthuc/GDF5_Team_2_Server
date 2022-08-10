package cmd.send.battle.player;

import bitzero.core.P;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseRequestUpgradeTower extends BaseMsg {
    private final short _error;
    private int towerId = 0;
    private int towerLevel = 0;
    private Point tilePos = null;
    private int tickNumber = 0;

    public ResponseRequestUpgradeTower(short _error, int towerId, int towerLevel, Point tilePos, int tickNumber) {
        super(CmdDefine.UPGRADE_TOWER);
        this._error = _error;
        this.towerId = towerId;
        this.towerLevel = towerLevel;
        this.tilePos = tilePos;
        this.tickNumber = tickNumber;
    }

    public ResponseRequestUpgradeTower(short _error) {
        super(CmdDefine.UPGRADE_TOWER, _error);
        this._error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        if (this._error == 0) {
            bf.putInt(towerId);
            bf.putInt(towerLevel);
            bf.putInt(tilePos.x);
            bf.putInt(tilePos.y);
            bf.putInt(tickNumber);
        }

        return packBuffer(bf);
    }
}
