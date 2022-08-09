package cmd.send.error;

import bitzero.server.exceptions.IErrorCode;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

/**
 * Created by hieupt on 11/8/18.
 */
public class ResponseError extends BaseMsg {
    private String errorMessage;

    public ResponseError(short error, String errorMessage) {
        super(CmdDefine.BATTLE_ERROR, error);
        this.errorMessage = errorMessage;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        putStr(bf,this.errorMessage);
        return packBuffer(bf);
    }

}

