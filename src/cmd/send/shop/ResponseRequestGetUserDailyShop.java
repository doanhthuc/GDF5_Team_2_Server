package cmd.send.shop;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.CardCollection;
import model.Item.Item;
import model.PlayerInfo;
import model.Shop.ItemList.DailyItemList;
import model.Shop.ShopItem;

import java.nio.ByteBuffer;

public class ResponseRequestGetUserDailyShop extends BaseMsg {
    public DailyItemList DIL;
    public short error;
    public ResponseRequestGetUserDailyShop(short _error, DailyItemList DIL) {
        super(CmdDefine.GET_DAILY_SHOP);
        this.DIL = DIL;
        error= _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.DIL.getSize());
        for(int i=0;i<this.DIL.getSize();i++)
        {
            ShopItem shopItem=this.DIL.itemList.get(i);
            bf.putInt(shopItem.getItemType());
            bf.putInt(shopItem.getQuantity());
            bf.putInt(shopItem.getPrice());
            bf.putInt(shopItem.getState());
        }
        System.out.println("ResponseRequestGetUserDailyShop");
        return packBuffer(bf);
    }
}