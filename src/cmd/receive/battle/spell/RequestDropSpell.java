package cmd.receive.battle.spell;

import battle.common.Point;
import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

public class RequestDropSpell extends BaseCmd {
    private int roomId;
    private int spellId;
    private Point pixelPos;
    public RequestDropSpell(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.roomId = readInt(bf);
            this.spellId = readInt(bf);
            this.pixelPos = new Point(readDouble(bf), readDouble(bf));
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getSpellId() {
        return spellId;
    }

    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }

    public Point getPixelPos() {
        return pixelPos;
    }

    public void setPixelPos(Point pixelPos) {
        this.pixelPos = pixelPos;
    }
}
