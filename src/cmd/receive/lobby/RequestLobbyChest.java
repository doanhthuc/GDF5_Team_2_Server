package cmd.receive.lobby;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestLobbyChest extends BaseCmd {
    private int lobbyChestId;

    public RequestLobbyChest(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            lobbyChestId = readInt(bf);
        } catch (Exception e) {
            lobbyChestId = -1;
            CommonHandle.writeErrLog(e);
        }
    }

    public int getLobbyChestId() {
        return this.lobbyChestId;
    }
}
