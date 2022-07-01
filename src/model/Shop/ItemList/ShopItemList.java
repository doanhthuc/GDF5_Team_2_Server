package model.Shop.ItemList;

import model.Shop.ShopItem;
import util.database.DataModel;

import java.util.ArrayList;

public class ShopItemList extends DataModel {
    public long userId;
    public ArrayList<ShopItem> itemList = new ArrayList<ShopItem>();

    public ShopItemList() {
    }

    public ShopItemList(long id, ArrayList<ShopItem> Goldbanner) {
        this.userId = id;
        for (int i = 0; i < Goldbanner.size(); i++) {
            ShopItem SI = new ShopItem(Goldbanner.get(i));
            this.addItem(SI);
        }
    }

    public void addItem(ShopItem shopItem) {
        this.itemList.add(shopItem);
    }

    public void show() {
        for (int i = 0; i < this.itemList.size(); i++) {
            this.itemList.get(i).show();
        }
    }

    public int getSize() {
        return this.itemList.size();
    }

    public ShopItem getItemByID(int itemID) {
        for (int i = 0; i < itemList.size(); i++){
            if (itemList.get(i).getItemID()==itemID) return itemList.get(i);
        }
        return null;
    }
}
