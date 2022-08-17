package cmd.receive.battle.tower;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.awt.*;
import java.nio.ByteBuffer;

public class RequestSpeedUpNextWave extends BaseCmd {
    private int roomId;

    public RequestSpeedUpNextWave(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.roomId = readInt(bf);
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getRoomId() {
        return roomId;
    }
}
