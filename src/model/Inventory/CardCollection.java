package model.Inventory;

import model.Item.ItemDefine;
import util.database.DataModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CardCollection extends DataModel {
    public int id;
    public ArrayList<Card> cardcollection = new ArrayList<Card>();
    public ArrayList<Integer> battleDeck = new ArrayList<Integer>();

    public CardCollection(int id) {
        this.id = id;
        this.cardcollection.add(new Card(ItemDefine.OWL, "", 6, 1, 50));
        this.cardcollection.add(new Card(ItemDefine.CROW, "", 9, 1, 50));
        this.cardcollection.add(new Card(ItemDefine.FROG, "", 10, 1, 50));
        this.cardcollection.add(new Card(ItemDefine.BUNNY, "", 6, 1, 50));
        this.cardcollection.add(new Card(ItemDefine.POLAR, "", 9, 1, 0));
        this.cardcollection.add(new Card(ItemDefine.GOAT, "", 10, 1, 0));
        this.cardcollection.add(new Card(ItemDefine.SNAKE, "", 6, 1, 0));
        this.cardcollection.add(new Card(ItemDefine.FIRE, "", 9, 1, 0));
        this.cardcollection.add(new Card(ItemDefine.SNOW, "", 10, 1, 0));
        this.cardcollection.add(new Card(ItemDefine.TRAP, "", 6, 1, 0));
        for (int i = 0; i < 8; i++)
            this.battleDeck.add(i);
    }

    public void upgradeCard(int cardType) {
        int level = cardcollection.get(cardType).getLevel();
        cardcollection.get(cardType).increaseLevel();
        cardcollection.get(cardType).increaseAccumulateCard(-CardDefine.fragmentToUpgrade.get(level));
    }

    public void updateCard(int cardType, int quantity) {
        this.cardcollection.get(cardType).increaseAccumulateCard(quantity);
    }

    public void show() {
        for (int i = 0; i < this.cardcollection.size(); i++) {
            this.cardcollection.get(i).show();
        }
    }

    public boolean checkFragmentToUpgradeCard(int cardType) {
        int currentLevel = this.cardcollection.get(cardType).getLevel();
        return this.cardcollection.get(cardType).getAmount() > CardDefine.fragmentToUpgrade.get(currentLevel);
    }

    public int getGoldToUpgradeCard(int cardType) {
        int currentLevel = this.cardcollection.get(cardType).getLevel();
        return CardDefine.goldToUpgrade.get(currentLevel);
    }

    public int getfragmentToUpgradeCard(int cardType) {
        int currentLevel = this.cardcollection.get(cardType).getLevel();
        return CardDefine.fragmentToUpgrade.get(currentLevel);
    }

    public int getSize() {
        return this.cardcollection.size();
    }

}
