package model.Chest;

import model.Item.Item;
import model.Item.ItemDefine;

import java.util.ArrayList;
import java.util.Random;
public class Chest {
    public int chesttype;
    public ArrayList<Item> reward= new ArrayList<Item>();
    public int cardSlot;
    public Chest(){
        this.chesttype=1;
        this.cardSlot=2;
        this.randomRewardItem(chesttype,cardSlot);
    }
    public Chest(int chesttype, int cardSlot)
    {
        this.chesttype=chesttype;
        this.cardSlot=cardSlot;
        this.randomRewardItem(chesttype,cardSlot);
    }
    public Chest(Chest ch){
        this.chesttype=ch.chesttype;
        this.cardSlot=ch.cardSlot;
        this.randomRewardItem(this.chesttype,this.cardSlot);
    }

    public void addReward(Item i) {
       this.reward.add(i);
    }

    public void randomRewardItem(int type, int cardSlot)
    {

        Random random = new Random();
        int goldquantity= random.nextInt(10)+10;
        Item GoldItem= new Item(ItemDefine.GOLDTYPE,goldquantity);
        this.addReward(GoldItem);
//
//        for(int i=1;i<=cardSlot;i++)
//        {
//            int cardtype = random.nextInt(ItemDefine.CARDAMOUNT)+1;
//            int quantity = random.nextInt(ChestDefine.MAXCARD-ChestDefine.MINCARD)+ChestDefine.MINCARD;
//            this.reward.add(new Item(cardtype,quantity));
//        }
    }
}
