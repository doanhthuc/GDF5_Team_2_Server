package model.Item;
import util.database.DataModel;
import util.server.ServerConstant;

import java.awt.*;

public class Item {
    // Zing me
    protected int itemType;
    protected int quantity;
    public  Item() {}
    public Item(int itemType,int quantity) {

       this.itemType=itemType;
       this.quantity=quantity;
    }
    public int getQuantity(){
        return this.quantity;
    }

    public int getItemType(){
        return this.itemType;
    }
    public void show()
    {
        if (this.itemType==ItemDefine.GOLDTYPE)  System.out.print("GOLD");
        else if (this.itemType==ItemDefine.CHESTYPE)  System.out.print("CHEST");
        else System.out.print("CARD" + this.itemType);
        System.out.println(" quantity="+ this.quantity);
    }
}
