package model;

import model.Item.Item;

import java.util.ArrayList;

public class ShopDTO {
    public int goldChange;
    public int gemchange;
    public ArrayList<Item> itemList;
    public ShopDTO(int goldChange,int gemchange)
    {
        this.goldChange=goldChange;
        this.gemchange=gemchange;
    }
    public int getGoldChange(){
        return this.goldChange;
    }
    public int getGemChange(){
        return this.gemchange;
    }
}
