package cmd.receive.inventory;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestUpgradeCard extends BaseCmd {
    private int cardType;

    public RequestUpgradeCard(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            cardType = readInt(bf);
        } catch (Exception e) {
            cardType = 0;
            CommonHandle.writeErrLog(e);
        }
    }

    public int getcardType() {
        return this.cardType;
    }
}
