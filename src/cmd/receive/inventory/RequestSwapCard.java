package cmd.receive.inventory;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestSwapCard extends BaseCmd {
    private int cardOutID;
    private int cardInID;

    public RequestSwapCard(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            cardOutID = readInt(bf);
            cardInID = readInt(bf);
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getCardOutID() {
        return this.cardOutID;
    }

    public int getCardInID() {
        return this.cardInID;
    }
}
