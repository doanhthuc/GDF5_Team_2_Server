package cmd.receive.cheat;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 * all packet receive from client will extends BaseCmd and override unpackData() function
 */
public class RequestCheatUserCard extends BaseCmd {
    public static final int DEFAULT = 0;
    private int cardType;
    private int cardLevel;
    private int cardAmount;
    public RequestCheatUserCard(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.cardType = readInt(bf);
            this.cardLevel= readInt(bf);
            this.cardAmount= readInt(bf);
        } catch (Exception e) {
            CommonHandle.writeErrLog(e);
        }
    }

    public int getCardType() {
        return this.cardType;
    }
    public int getCardLevel() { return this.cardLevel; }
    public int getCardAmount() { return this.cardAmount; }
}
