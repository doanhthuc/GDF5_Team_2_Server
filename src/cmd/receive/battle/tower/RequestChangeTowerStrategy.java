package cmd.receive.battle.tower;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.awt.*;
import java.nio.ByteBuffer;

public class RequestChangeTowerStrategy extends BaseCmd {
    private int roomId;
    private int strategyId;
    private Point tilePos;

    public RequestChangeTowerStrategy(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.roomId = readInt(bf);
            this.tilePos = new Point(readInt(bf), readInt(bf));
            this.strategyId = readInt(bf);
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public int getStrategyId() {
        return strategyId;
    }

    public Point getTilePos() {
        return tilePos;
    }
}
