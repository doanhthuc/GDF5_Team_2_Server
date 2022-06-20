package cmd.receive.shop;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestBuyGoldShop extends BaseCmd {
    public static final int SHOPGOLD_ID_DEFAULT = 0;
    private int shopGoldId;
    public RequestBuyGoldShop(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            shopGoldId = readInt(bf);
        } catch (Exception e) {
            shopGoldId = SHOPGOLD_ID_DEFAULT;
            CommonHandle.writeErrLog(e);
        }
    }

    public int getId(){
        return this.shopGoldId;
    }
}
