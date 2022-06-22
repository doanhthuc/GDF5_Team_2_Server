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
        super(CmdDefine.GET_USER_INVENTORY);
        this.cc = cc;
        error= _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.cc.cardcollection.size());
        for(int i=0;i<this.cc.cardcollection.size();i++)
        {
            int cardtype=this.cc.cardcollection.get(i).getCardType();
            int level =this.cc.cardcollection.get(i).getLevel();
            int amount= this.cc.cardcollection.get(i).getAmount();
            //System.out.println("Cardtype="+cardtype+" level="+ level+" amount="+amount);
            bf.putInt(cardtype);
            bf.putInt(level);
            bf.putInt(amount);
        }
        bf.putInt(this.cc.battleDeck.size());
        for(int i=0;i<this.cc.battleDeck.size();i++)
            bf.putInt(cc.battleDeck.get(i));
        System.out.println("ResponseRequestGetUserInventory");
        return packBuffer(bf);
    }
}