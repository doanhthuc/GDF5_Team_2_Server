package cmd.receive.user;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestAddGem extends BaseCmd {
    public static final int GEM_DEFAULT = 0;
    private int gem;

    public RequestAddGem(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            gem = readInt(bf);
        } catch (Exception e) {
            gem = GEM_DEFAULT;
            CommonHandle.writeErrLog(e);
        }
    }

    public int getGem() {
        return this.gem;
    }
}
