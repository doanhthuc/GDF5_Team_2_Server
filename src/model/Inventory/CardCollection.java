package model.Inventory;

import model.Item.ItemDefine;
import util.database.DataModel;

import java.util.ArrayList;

public class CardCollection extends DataModel {
    public long id;
    public ArrayList<Card> cardCollection = new ArrayList<Card>();
    public ArrayList<Integer> battleDeck = new ArrayList<Integer>();

    public CardCollection(long id) {
        this.id = id;
        this.cardCollection.add(new Card(ItemDefine.OWL, "", 6, 1, 50));
        this.cardCollection.add(new Card(ItemDefine.CROW, "", 9, 1, 50));
        this.cardCollection.add(new Card(ItemDefine.FROG, "", 10, 1, 50));
        this.cardCollection.add(new Card(ItemDefine.BUNNY, "", 6, 1, 50));
        this.cardCollection.add(new Card(ItemDefine.POLAR, "", 9, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.GOAT, "", 10, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.SNAKE, "", 6, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.FIRE, "", 9, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.SNOW, "", 10, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.TRAP, "", 6, 1, 0));
        for (int i = 0; i < 8; i++)
            this.battleDeck.add(i);
    }

    public void upgradeCard(int cardType) {
        int level = cardCollection.get(cardType).getLevel();
        cardCollection.get(cardType).increaseLevel();
        cardCollection.get(cardType).increaseAccumulateCard(-CardDefine.fragmentToUpgrade.get(level));
    }

    public void updateCard(int cardType, int quantity) {
        this.cardCollection.get(cardType).increaseAccumulateCard(quantity);
    }

    public void show() {
        for (int i = 0; i < this.cardCollection.size(); i++) {
            this.cardCollection.get(i).show();
        }
    }

    public boolean checkFragmentToUpgradeCard(int cardType) {
        int currentLevel = this.cardCollection.get(cardType).getLevel();
        return this.cardCollection.get(cardType).getAmount() > CardDefine.fragmentToUpgrade.get(currentLevel);
    }

    public int getGoldToUpgradeCard(int cardType) {
        int currentLevel = this.cardCollection.get(cardType).getLevel();
        return CardDefine.goldToUpgrade.get(currentLevel);
    }

    public int getfragmentToUpgradeCard(int cardType) {
        int currentLevel = this.cardCollection.get(cardType).getLevel();
        return CardDefine.fragmentToUpgrade.get(currentLevel);
    }

    public void setCard(int cardType, int cardLevel, int cardAmount)
    {
        this.cardCollection.get(cardType).setLevel(cardLevel);
        this.cardCollection.get(cardType).setAmount(cardAmount);
    }
    public int getSize() {
        return this.cardCollection.size();
    }

}
