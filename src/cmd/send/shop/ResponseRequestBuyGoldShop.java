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
        this.shopDTO=shopDTO;
        error= _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(shopDTO.getGoldChange());
        bf.putInt(shopDTO.getGemChange());
        //System.out.println("response buy shop gold");
//        for(int i=0;i<shopDTO.itemList.size();i++){
//            bf.putInt(shopDTO.itemList.get(i).getItemType());
//            bf.putInt(shopDTO.itemList.get(i).getQuantity());
//        }
        return packBuffer(bf);
    }
}