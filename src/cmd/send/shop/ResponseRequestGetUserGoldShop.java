package cmd.send.shop;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Shop.ItemList.DailyItemList;
import model.Shop.ItemList.ShopItemList;
import model.Shop.ShopItem;

import java.nio.ByteBuffer;

public class ResponseRequestGetUserGoldShop extends BaseMsg {
    public ShopItemList goldShop;
    public short error;

    public ResponseRequestGetUserGoldShop(short _error, ShopItemList goldShop) {
        super(CmdDefine.GET_GOLD_SHOP);
        this.goldShop = goldShop;
        error = _error;
    }
    public ResponseRequestGetUserGoldShop(short _error) {
        super(CmdDefine.GET_GOLD_SHOP);
        error = _error;
    }


    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(this.goldShop.getSize());
        for (int i = 0; i < this.goldShop.getSize(); i++) {
            ShopItem shopItem = this.goldShop.itemList.get(i);
            bf.putInt(shopItem.getItemType());
            bf.putInt(shopItem.getQuantity());
            bf.putInt(shopItem.getPrice());
            bf.putInt(shopItem.getState());
        }
        return packBuffer(bf);
    }
}