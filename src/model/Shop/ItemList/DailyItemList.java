package model.Shop.ItemList;
import model.Chest.Chest;
import model.Item.ItemDefine;
import model.Shop.ShopChestItem;
import model.Shop.ShopItem;

public class DailyItemList extends ShopItemList {
    public int remainingTime;
    public DailyItemList(int id)
    {
        this.id=id;
        //System.out.println(("DailyItemList"));
        this.itemList.add(new ShopItem(ItemDefine.CHESTYPE,1, ShopItemDefine.CAN_BUY, ShopItemDefine.DAILY_CHEST_PRICE));
        for(int i=1;i<=2;i++)
        {
            ShopItem shopCarditem = new ShopItem();
            shopCarditem.randomCardItem();
            this.itemList.add(shopCarditem);
        }
    }
    public int getSize(){
        return this.itemList.size();
    }
}
