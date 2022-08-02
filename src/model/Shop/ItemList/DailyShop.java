package model.Shop.ItemList;

import battle.config.GameConfig;
import model.Common.ItemDefine;
import model.Shop.ShopItem;

import java.util.*;

public class DailyShop extends ShopItemList {
    private long futureResetTime;

    public DailyShop(long userId) {
        this.userId = userId;
        //System.out.println(("DailyItemList"));
        this.itemList.add(new ShopItem(0,ItemDefine.CHESTTYPE, 1, ShopItemDefine.CAN_BUY, ShopItemDefine.DAILY_CHEST_PRICE));
        Map<Integer, Boolean> existCardType = new HashMap();
        Random random = new Random();

        for (int i = 1; i <= 2; i++) {
            ShopItem shopCardItem = new ShopItem();

            // check duplicate random card Type
            int cardType = random.nextInt(ItemDefine.CARDAMOUNT);
            while (existCardType.containsKey(cardType)) {
                cardType = random.nextInt(ItemDefine.CARDAMOUNT);
            }
            existCardType.put(cardType, true);

            shopCardItem.randomCardItem(i, cardType);
            this.itemList.add(shopCardItem);
        }

        long currentTime = (new Date()).getTime();
        this.futureResetTime = currentTime + 1000 * GameConfig.DAILY_SHOP_RESET_TIME_SECOND;
    }

    public boolean isBeforeResetTime () {
        long currentTime = (new Date()).getTime();
        return currentTime <= this.futureResetTime;
    }

    public int getSize() {
        return this.itemList.size();
    }

    public long getFutureResetTime() {
        return futureResetTime;
    }
}
