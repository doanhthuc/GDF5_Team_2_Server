package model.Shop.ItemList;

import model.Shop.ShopItem;
import util.database.DataModel;

import java.util.ArrayList;

public class ShopItemList extends DataModel {
    public int id;
    public ArrayList<ShopItem> itemList= new ArrayList<ShopItem>();
    public ShopItemList(){
    }
    public ShopItemList(int id,ArrayList<ShopItem>Goldbanner){
        this.id=id;
        for(int i=0;i<Goldbanner.size();i++)
        {
            ShopItem SI= new ShopItem(Goldbanner.get(i));
            this.addItem(SI);
        }
    }
    public void addItem(ShopItem shopItem)
    {
        this.itemList.add(shopItem);
    }
    public void show()
    {
        for(int i=0;i<this.itemList.size();i++)
        {
            this.itemList.get(i).show();
        }
    }
}
