package cmd.send.inventory;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.InventoryDTO;

import java.nio.ByteBuffer;

public class ResponseRequestSwapCard extends BaseMsg {
    public int cardInID ;
    public int cardOutID ;
    public short error;

    public ResponseRequestSwapCard(short _error) {
        super(CmdDefine.UPGRADE_CARD,_error);
    }
    public ResponseRequestSwapCard(short _error, int cardInID, int cardOutID) {
        super(CmdDefine.UPGRADE_CARD,_error);
        this.cardInID = cardInID;
        this.cardOutID = cardOutID;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.cardInID);
        bf.putInt(this.cardOutID);
        return packBuffer(bf);
    }
}