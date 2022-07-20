package cmd.receive.battle.tower;



import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.awt.*;
import java.nio.ByteBuffer;

public class RequestPutTower extends BaseCmd {

    private int roomId;
    private int towerId;
    private Point tilePos;

    public RequestPutTower(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.roomId = readInt(bf);
            this.towerId = readInt(bf);
            this.tilePos = new Point(readInt(bf), readInt(bf));
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public Point getTilePos() {
        return tilePos;
    }

    public void setTilePos(Point tilePos) {
        this.tilePos = tilePos;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTowerId() {
        return towerId;
    }

    public void setTowerId(int towerId) {
        this.towerId = towerId;
    }
}
