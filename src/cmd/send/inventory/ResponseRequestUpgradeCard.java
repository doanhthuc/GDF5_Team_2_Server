package cmd.send.inventory;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.InventoryDTO;

import java.nio.ByteBuffer;

public class ResponseRequestUpgradeCard extends BaseMsg {
    public InventoryDTO inventoryDTO;
    public short error;

    public ResponseRequestUpgradeCard(short _error, InventoryDTO inventoryDTO) {
        super(CmdDefine.UPGRADE_CARD, _error);
        this.inventoryDTO = inventoryDTO;
        error = _error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(inventoryDTO.getGoldChange());
        bf.putInt(inventoryDTO.getCardType());
        bf.putInt(inventoryDTO.getFragmentChange());
        return packBuffer(bf);
    }
}