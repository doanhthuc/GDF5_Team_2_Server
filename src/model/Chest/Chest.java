package model;

import java.util.ArrayList;
import java.util.Random;
public class Chest {
    protected int chesttype;
    protected ArrayList<Item> reward;
    protected int cardSlot;
    public Chest(){
        this.chesttype=1;
        this.cardSlot=2;
        this.randomRewardItem(this.chesttype,this.cardSlot);
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
    public void randomRewardItem(int type, int cardSlot)
    {
        Random random = new Random();
        int goldquantity= random.nextInt(1)+1;
        this.reward.add(new Item(1,goldquantity));
        this.reward.add(new Item());
        for(int i=1;i<=cardSlot;i++)
        {
            int cardtype = random.nextInt(1)+1;
            int quantity = random.nextInt(1)+1;
            this.reward.add(new Item(cardtype,quantity));
        }
    }
}
