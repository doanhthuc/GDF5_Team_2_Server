package cmd.receive.authen;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.nio.ByteBuffer;

public class RequestLogin extends BaseCmd {
    public String sessionKey = "";
    public String userIDStr = "";
    public RequestLogin(DataCmd dataCmd) {
        super(dataCmd);
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            sessionKey = readString(bf);
            userIDStr = readString(bf);
        } catch (Exception e) {

        }
    }

}
