package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestAddGold extends BaseCmd {
    public static final int GOLD_DEFAULT = 0;
    private int gold;
    public RequestAddGold(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            gold = readInt(bf);
        } catch (Exception e) {
            gold = GOLD_DEFAULT;
            CommonHandle.writeErrLog(e);
        }
    }

    public int getGold(){
        return this.gold;
    }
}
