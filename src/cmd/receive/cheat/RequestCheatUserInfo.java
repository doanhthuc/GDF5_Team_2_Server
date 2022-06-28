package cmd.receive.cheat;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestCheatUserInfo extends BaseCmd {
    private int gold;
    private int gem;
    private int trophy;
    public RequestCheatUserInfo(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.gem = readInt(bf);
            this.gold= readInt(bf);
            this.trophy= readInt(bf);
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getGold() {
        return this.gold;
    }
    public int getGem() { return this.gem; }
    public int getTrophy() { return this.trophy; }
}
