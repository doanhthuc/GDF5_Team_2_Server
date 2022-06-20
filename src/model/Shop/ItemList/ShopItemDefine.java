package model.Shop.ItemList;
import model.Item.ItemDefine;
import model.Shop.ShopItem;

import java.util.ArrayList;

public class ShopItemDefine {
    public static ArrayList<ShopItem> GoldBanner = new ArrayList<ShopItem>(){{
        add(new ShopItem(ItemDefine.GOLDTYPE,1000,1,50));
        add(new ShopItem(ItemDefine.GOLDTYPE,2000,1,95));
        add(new ShopItem(ItemDefine.GOLDTYPE,10000,1,475));
    }} ;
    public static int CAN_BUY=1;
    public static int CAN_NOT_BUY=0;
    public static int DAILY_CHEST_PRICE=600;
}
