package cmd.send.matching;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

public class ResponseCancelMatching extends BaseMsg {
    public ResponseCancelMatching() {
        super(CmdDefine.CANCEL_MATCHING);
    }
}
