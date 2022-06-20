package cmd.send.inventory;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.CardCollection;
import model.PlayerInfo;

import java.nio.ByteBuffer;

public class ResponseRequestGetUserInventory extends BaseMsg {
    public CardCollection cc;
    public short error;
    public ResponseRequestGetUserInventory(short _error, CardCollection cc) {
        super(CmdDefine.GET_USER_INFO);
        this.cc = cc;
        error= _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.cc.getSize());
        for(int i=0;i<this.cc.getSize();i++)
        {
            bf.putInt(this.cc.cardcollection.get(i).getCardType());
            bf.putInt(this.cc.cardcollection.get(i).getLevel());
            bf.putInt(this.cc.cardcollection.get(i).getAmount());
        }
        return packBuffer(bf);
    }
}