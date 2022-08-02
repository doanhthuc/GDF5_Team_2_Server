package cmd.send.shop;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Shop.ItemList.DailyShop;
import model.Shop.ShopItem;

import java.nio.ByteBuffer;

public class ResponseRequestGetUserDailyShop extends BaseMsg {
    public DailyShop dailyShop;
    public short error;

    public ResponseRequestGetUserDailyShop(short _error, DailyShop dailyShop) {
        super(CmdDefine.GET_DAILY_SHOP);
        this.dailyShop = dailyShop;
        error = _error;
    }
    public ResponseRequestGetUserDailyShop(short _error) {
        super(CmdDefine.GET_DAILY_SHOP);
        error = _error;
    }


    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.dailyShop.getSize());
        for (int i = 0; i < this.dailyShop.getSize(); i++) {
            ShopItem shopItem = this.dailyShop.itemList.get(i);
            bf.putInt(shopItem.getItemID());
            bf.putInt(shopItem.getItemType());
            bf.putInt(shopItem.getQuantity());
            bf.putInt(shopItem.getPrice());
            bf.putInt(shopItem.getState());
        }
        bf.putLong(this.dailyShop.getFutureResetTime());
        return packBuffer(bf);
    }
}