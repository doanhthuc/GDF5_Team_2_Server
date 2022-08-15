package cmd.send.battle.opponent;

import battle.common.Point;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseOpponentDropSpell extends BaseMsg {
    private short _error;
    private final int spellId;
    private final int spellLevel;
    private final Point pixelPos;
    private final int tickNumber;

    public ResponseOpponentDropSpell(short _error, int spellId, int spellLevel, Point pixelPos, int tickNumber) {
        super(CmdDefine.OPPONENT_DROP_SPELL);
        this._error = _error;
        this.spellId = spellId;
        this.spellLevel = spellLevel;
        this.pixelPos = pixelPos;
        this.tickNumber = tickNumber;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(spellId);
        bf.putInt(spellLevel);
        bf.putDouble(pixelPos.x);
        bf.putDouble(pixelPos.y);
        bf.putInt(tickNumber);
        return packBuffer(bf);
    }
}
