package model;

import cmd.obj.demo.DemoDirection;
import cmd.obj.demo.MaxPosition;
import util.database.DataModel;
import util.server.ServerConstant;

import java.awt.*;

public class Item {
    // Zing me
    protected int itemType;
    protected int quantity;
    public  Item()
    {

    }
    public Item(int itemType,int quantity) {
        super();
       this.itemType=itemType;
       this.quantity=quantity;
    }

    public void show()
    {
       System.out.println(this.itemType+" "+this.quantity);
    }
}
