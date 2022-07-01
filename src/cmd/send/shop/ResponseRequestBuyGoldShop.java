package cmd.send.shop;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Shop.ShopDTO;

import java.nio.ByteBuffer;

public class ResponseRequestBuyGoldShop extends BaseMsg {
    public ShopDTO shopDTO;
    public short error;

    public ResponseRequestBuyGoldShop(short _error, ShopDTO shopDTO) {
        super(CmdDefine.BUY_GOLD_SHOP);
        this.shopDTO = shopDTO;
        error = _error;
    }
    public ResponseRequestBuyGoldShop(short _error) {
        super(CmdDefine.BUY_GOLD_SHOP);
        error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(this.shopDTO.getItemID());
        bf.putInt(this.shopDTO.getGoldChange());
        bf.putInt(this.shopDTO.getGemChange());
        return packBuffer(bf);
    }
}