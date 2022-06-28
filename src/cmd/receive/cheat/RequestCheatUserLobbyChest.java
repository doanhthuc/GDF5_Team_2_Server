package cmd.receive.cheat;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestCheatUserLobbyChest extends BaseCmd {
    public static final int DEFAULT = 0;
    private int chestId;
    private int chestState;
    private int chestRemainingTime;
    public RequestCheatUserLobbyChest(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.chestId = readInt(bf);
            this.chestState= readInt(bf);
            this.chestRemainingTime= readInt(bf);
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getChestId() {
        return this.chestId;
    }
    public int getChestState() { return this.chestState; }
    public int getChestRemainingTime() { return this.chestRemainingTime; }
}
