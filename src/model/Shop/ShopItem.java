package model.Shop;

import model.Item.Item;
import model.Item.ItemDefine;
import model.Shop.ItemList.ShopItemDefine;

import java.util.Random;


public class ShopItem extends Item {
    protected int itemId;
    protected int state;
    protected int price;

    public ShopItem() {
        this.price = 1;
        this.state = 1;
        this.itemType = 1;
        this.quantity = 1;
    }

    public ShopItem(ShopItem shopItem) {
        this.itemId=shopItem.itemId;
        this.price = shopItem.price;
        this.state = shopItem.state;
        this.itemType = shopItem.itemType;
        this.quantity = shopItem.quantity;
    }

    public ShopItem(int itemId,int type, int quantity, int state, int price) {
        this.itemId=itemId;
        this.price = price;
        this.quantity = quantity;
        this.itemType = type;
        this.state = state;
    }

    public void show() {
        if (this.itemType == ItemDefine.GOLDTYPE) System.out.print("GOLD");
        else if (this.itemType == ItemDefine.CHESTTYPE) System.out.print("CHEST");
        else System.out.print("CARD" + this.itemType);
        System.out.print(" price=" + this.price);
        System.out.print(" quantity=" + this.quantity);
        if (this.state == 1) System.out.println(" Can Buy");
        else System.out.println("Cannot Buy");
    }

    public void randomCardItem(int itemId) {
        Random random = new Random();
        this.itemId=itemId;
        this.itemType = random.nextInt(ItemDefine.CARDAMOUNT);
        this.quantity = (random.nextInt(ShopItemDefine.MAX_CARD -ShopItemDefine.MIN_CARD+ 1) + ShopItemDefine.MIN_CARD) * ShopItemDefine.MULTI;
        this.price = this.quantity * ShopItemDefine.PRICE_PER_CARD;
        this.state = ShopItemDefine.CAN_BUY;
    }

    public int getPrice() {
        return this.price;
    }

    public int getState() {
        return this.state;
    }
    public int getItemID(){
        return this.itemId;
    }
    public void setState(int state) {
        this.state = state;
    }
}


