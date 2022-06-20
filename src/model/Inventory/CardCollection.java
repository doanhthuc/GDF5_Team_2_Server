package model.Inventory;

import model.Item.ItemDefine;
import util.database.DataModel;
import java.util.ArrayList;
import java.util.HashMap;

public class CardCollection extends DataModel {
    public int id;
    public ArrayList<Card> cardcollection = new ArrayList<Card>();
    public CardCollection(int id){
        this.id=id;
        this.cardcollection.add(new Card(ItemDefine.OWL,"",6,1,0));
        this.cardcollection.add(new Card(ItemDefine.CROW,"",9,1,0));
        this.cardcollection.add(new Card(ItemDefine.FROG,"",10,1,0));
        this.cardcollection.add(new Card(ItemDefine.BUNNY,"",6,0,0));
        this.cardcollection.add(new Card(ItemDefine.POLAR,"",9,0,0));
        this.cardcollection.add(new Card(ItemDefine.GOAT,"",10,0,0));
        this.cardcollection.add(new Card(ItemDefine.SNAKE,"",6,0,0));
        this.cardcollection.add(new Card(ItemDefine.FIRE,"",9,0,0));
        this.cardcollection.add(new Card(ItemDefine.SNOW,"",10,0,0));
        this.cardcollection.add(new Card(ItemDefine.TRAP,"",6,0,0));
    }
    public void upgradeCard(int cardType)
    {
        for(int i=0;i<=cardcollection.size();i++)
        {
            if (cardcollection.get(i).getCardType()==cardType) cardcollection.get(i).increaseLevel();
        }
    }
    public void updateCard(int cardType, int quantity)
    {
        for(int i=0;i<=this.cardcollection.size();i++)
        {
            if (this.cardcollection.get(i).cardType== cardType) this.cardcollection.get(i).increaseAccumulateCard(quantity);
            if (this.cardcollection.get(i).level==0) this.cardcollection.get(i).increaseLevel();
        }

    }
    public void show(){
        for(int i=0;i<this.cardcollection.size();i++)
        {
            if (this.cardcollection.get(i).getLevel()!=0)
                this.cardcollection.get(i).show();
        }
    }
    public int getSize(){
        return this.cardcollection.size();
    }

}
