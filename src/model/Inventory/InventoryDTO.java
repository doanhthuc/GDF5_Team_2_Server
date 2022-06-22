package model.Inventory;

import model.Item.Item;

import java.util.ArrayList;

public class InventoryDTO {
    public int goldChange;
    public int cardType;
    public int fragmentChange;
    public InventoryDTO(int goldChange,int cardType,int fragmentChange)
    {
        this.goldChange=goldChange;
        this.cardType=cardType;
        this.fragmentChange=fragmentChange;
    }
    public int getGoldChange()
    {
        return this.goldChange;
    }
    public int getCardType()
    {
        return this.cardType;
    }
    public int getFragmentChange()
    {
        return this.fragmentChange;
    }
}
