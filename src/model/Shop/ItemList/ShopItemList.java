package model.Shop;

import util.database.DataModel;

import java.util.ArrayList;

public class ShopItemList extends DataModel {
    public int id;
    public ArrayList<ShopItem> itemList;
    public ShopItemList()
    {
    }
    public void addItem(ShopItem shopItem)
    {
        this.itemList.add(shopItem);
    }
}
