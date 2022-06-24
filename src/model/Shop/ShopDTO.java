package model.Shop;

import model.Item.Item;

import java.util.ArrayList;

public class ShopDTO {
    public int goldChange;
    public int gemChange;
    public ArrayList<Item> itemList = new ArrayList<Item>();

    public ShopDTO(int goldChange, int gemChange) {
        this.goldChange = goldChange;
        this.gemChange = gemChange;
    }

    public ShopDTO(int goldChange, int gemChange, ArrayList<Item> itemList) {
        this.gemChange = gemChange;
        this.goldChange = goldChange;
        for (int i = 0; i < itemList.size(); i++)
            this.itemList.add(itemList.get(i));
    }

    public int getGoldChange() {
        return this.goldChange;
    }

    public int getGemChange() {
        return this.gemChange;
    }
}
