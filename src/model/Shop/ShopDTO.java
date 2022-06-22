package model.Shop;

import model.Item.Item;

import java.util.ArrayList;

public class ShopDTO {
    public int goldChange;
    public int gemchange;
    public ArrayList<Item> itemList=new ArrayList<Item>();
    public ShopDTO(int goldChange,int gemchange)
    {
        this.goldChange=goldChange;
        this.gemchange=gemchange;
    }
    public ShopDTO(int goldChange,int gemchange, ArrayList<Item> itemList)
    {
        System.out.println("ShopDTO");
        this.gemchange=gemchange;
        this.goldChange=goldChange;
        for(int i=0;i<itemList.size();i++)
            this.itemList.add(itemList.get(i));
    }
    public int getGoldChange(){
        return this.goldChange;
    }
    public int getGemChange(){
        return this.gemchange;
    }
}
