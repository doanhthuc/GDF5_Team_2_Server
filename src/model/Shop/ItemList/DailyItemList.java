package model.Shop.ItemList;

import model.Item.ItemDefine;
import model.Shop.ShopItem;

public class DailyItemList extends ShopItemList {
    public int remainingTime;

    public DailyItemList(long userId) {
        this.userId = userId;
        //System.out.println(("DailyItemList"));
        //this.itemList.add(new ShopItem(0,ItemDefine.CHESTTYPE, 1, ShopItemDefine.CAN_BUY, ShopItemDefine.DAILY_CHEST_PRICE));
        for (int i = 0; i <= 2; i++) {
            ShopItem shopCardItem = new ShopItem();
            shopCardItem.randomCardItem(i);
            this.itemList.add(shopCardItem);
        }
    }

    public int getSize() {
        return this.itemList.size();
    }
}
