package model.Shop.ItemList;

import model.Common.ItemDefine;
import model.Shop.ShopItem;

public class DailyShop extends ShopItemList {
    public int remainingTime;

    public DailyShop(long userId) {
        this.userId = userId;
        //System.out.println(("DailyItemList"));
        this.itemList.add(new ShopItem(0,ItemDefine.CHESTTYPE, 1, ShopItemDefine.CAN_BUY, ShopItemDefine.DAILY_CHEST_PRICE));
        for (int i = 1; i <= 2; i++) {
            ShopItem shopCardItem = new ShopItem();
            shopCardItem.randomCardItem(i);
            this.itemList.add(shopCardItem);
        }
    }

    public int getSize() {
        return this.itemList.size();
    }
}
