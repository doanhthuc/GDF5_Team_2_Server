package cmd.send.battle;

import battle.common.Point;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseOpponentDropSpell extends BaseMsg {
    private short _error;
    private final int spellId;
    private final int spellLevel;
    private final Point pixelPos;

    public ResponseOpponentDropSpell(short _error, int spellId, int spellLevel, Point pixelPos) {
        super(CmdDefine.OPPONENT_DROP_SPELL);
        this._error = _error;
        this.spellId = spellId;
        this.spellLevel = spellLevel;
        this.pixelPos = pixelPos;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(spellId);
        bf.putInt(spellLevel);
        bf.putDouble(pixelPos.x);
        bf.putDouble(pixelPos.y);
        return packBuffer(bf);
    }
}
