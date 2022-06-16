package model;

public class ShopItem extends Item implements purchaseType{
    protected int state;
    protected int price;
    public ShopItem(int type,int quantity, int state, int price)
    {
        this.price=price;
        this.quantity=quantity;
        this.itemType=type;
        this.state=state;
    }
}

interface IpurchaseType{
    void purchase();
}
