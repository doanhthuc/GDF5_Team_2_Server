package model.Shop;

import model.Chest.ChestDefine;
import model.Item.Item;
import model.Item.ItemDefine;
import model.Shop.ItemList.ShopItemDefine;

import java.util.Random;

interface purchaseType {
    void purchase();
}
public class ShopItem extends Item implements purchaseType{
    protected int state;
    protected int price;
    public  ShopItem()
    {
        this.price=1;
        this.state=1;
        this.itemType=1;
        this.quantity=1;
    }
    public  ShopItem(ShopItem SI)
    {
        this.price=SI.price;
        this.state=SI.state;
        this.itemType=SI.itemType;
        this.quantity=SI.quantity;
    }
    public ShopItem(int type,int quantity, int state, int price)
    {
        this.price=price;
        this.quantity=quantity;
        this.itemType=type;
        this.state=state;
    }
    public void show()
    {
        if (this.itemType==ItemDefine.GOLDTYPE)  System.out.print("GOLD");
        else if (this.itemType==ItemDefine.CHESTYPE)  System.out.print("CHEST");
        else System.out.print("CARD" + this.itemType);
        System.out.print(" price="+this.price);
        System.out.print(" quantity="+ this.quantity);
        if (this.state==1) System.out.println(" Can Buy");
        else System.out.println("Cannot Buy");
    }
    public void randomCardItem(){
        Random random= new Random();
        this.itemType= random.nextInt(ItemDefine.CARDAMOUNT);
        this.quantity= (random.nextInt(ShopItemDefine.MAX_CARD+1)+ShopItemDefine.MAX_CARD-ShopItemDefine.MIN_CARD)*ShopItemDefine.MULTI;
        this.price=this.quantity*ShopItemDefine.PRICE_PER_CARD;
        this.state= ShopItemDefine.CAN_BUY;
    }
    public int getPrice(){ return this.price; }
    public int getState(){ return this.state; }
    public void setState(int state) { this.state=state;}
    public void purchase(){
    }
}


