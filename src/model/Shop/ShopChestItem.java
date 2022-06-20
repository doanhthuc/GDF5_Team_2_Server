package model.Shop;

import model.Chest.Chest;

public class ShopChestItem extends ShopItem{
    private String name;
    private Chest chest;
    public ShopChestItem()
    {

    }
    public ShopChestItem(int itemType, int quantity, int state, int price, String name)
    {

        this.itemType=itemType;
        this.quantity=quantity;
        this.state=state;
        this.price=price;
        System.out.println("ShopCHESTITEM");
        this.chest = new Chest();
        this.name= name;

    }

}
