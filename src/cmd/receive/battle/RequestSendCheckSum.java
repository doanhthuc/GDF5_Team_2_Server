package cmd.receive.battle.tower;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.awt.*;
import java.nio.ByteBuffer;

public class RequestSendCheckSum extends BaseCmd {
    private int roomId;
    private int tickAmount;
    private double[] sumHpInEachTick = new double[100000];

    public RequestSendCheckSum(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.roomId = readInt(bf);
            this.tickAmount = readInt(bf);
            for (int i = 0; i < tickAmount; i++)
            {
                this.sumHpInEachTick[i] = readDouble(bf);
            }
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public int getTickAmount() {
        return tickAmount;
    }

    public double[] getSumHpInTick() {
        return sumHpInEachTick;
    }

}
