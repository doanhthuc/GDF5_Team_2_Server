package cmd.send.battle.opponent;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseOpponentChangeTowerTargetStrategy extends BaseMsg {
    private final short _error;
    private final int strategyId;
    private final Point tilePos;

    public ResponseOpponentChangeTowerTargetStrategy(short _error, int strategyId, Point tilePos) {
        super(CmdDefine.OPPONENT_CHANGE_TOWER_STRATEGY);
        this._error = _error;
        this.strategyId = strategyId;
        this.tilePos = tilePos;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(tilePos.x);
        bf.putInt(tilePos.y);
        bf.putInt(strategyId);
        return packBuffer(bf);
    }
}
