package cmd.receive.shop;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestBuyDailyShop extends BaseCmd {
    public static final int SHOPDAILY_ID_DEFAULT = 0;
    private int itemId;
    public RequestBuyDailyShop(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            itemId = readInt(bf);
        } catch (Exception e) {
            itemId = readInt(bf);
            CommonHandle.writeErrLog(e);
        }
    }

    public int getId(){
        return this.itemId;
    }
}
