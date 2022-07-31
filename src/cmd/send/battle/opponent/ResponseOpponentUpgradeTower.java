package cmd.send.battle.opponent;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseOpponentUpgradeTower extends BaseMsg {
    private final short _error;
    private final int towerId;
    private final int towerLevel;
    private final Point tilePos;

    public ResponseOpponentUpgradeTower(short _error, int towerId, int towerLevel, Point tilePos) {
        super(CmdDefine.OPPONENT_UPGRADE_TOWER);
        this._error = _error;
        this.towerId = towerId;
        this.towerLevel = towerLevel;
        this.tilePos = tilePos;
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
