package model.Inventory;

import model.Common.ItemDefine;
import util.database.DataModel;

import java.util.ArrayList;

public class Inventory extends DataModel {
    public long id;
    public ArrayList<Card> cardCollection = new ArrayList<Card>();
    public ArrayList<Integer> battleDeck = new ArrayList<Integer>();

    public Inventory(long id) {
        this.id = id;
        this.cardCollection.add(new Card(ItemDefine.OWL, "", 6, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.CROW, "", 9, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.FROG, "", 10, 1, 0));
        this.cardCollection.add(new Card(ItemDefine.BUNNY, "", 6, 1, 0));
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
        Card cardToUpgrade = null;
        for (Card i : this.cardCollection) {
            if (i.getCardType() == cardType) cardToUpgrade = i;
        }
        int level = cardToUpgrade.getLevel();
        cardToUpgrade.increaseLevel();
        cardToUpgrade.increaseAccumulateCard(-CardDefine.fragmentToUpgrade.get(level));
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
        Card card = null;
        for (Card i : this.cardCollection) {
            if (i.getCardType() == cardType) card = i;
        }
        int currentLevel = card.getLevel();
        return card.getAmount() > CardDefine.fragmentToUpgrade.get(currentLevel);
    }

    public int getGoldToUpgradeCard(int cardType) {
        Card card = null;
        for (Card i : this.cardCollection) {
            if (i.getCardType() == cardType) card = i;
        }
        int currentLevel = card.getLevel();
        return CardDefine.goldToUpgrade.get(currentLevel);
    }

    public int getfragmentToUpgradeCard(int cardType) {
        Card card = null;
        for (Card i : this.cardCollection) {
            if (i.getCardType() == cardType) card = i;
        }
        int currentLevel = card.getLevel();
        return CardDefine.fragmentToUpgrade.get(currentLevel);
    }

    public Card getCardById(int cardId) {
        for (Card card : this.cardCollection) {
            if (card.getCardType() == cardId) {
                return card;
            }
        }
        return null;
    }

    public void setCard(int cardType, int cardLevel, int cardAmount) {
        Card card = null;
        for (Card i : this.cardCollection) {
            if (i.getCardType() == cardType) card = i;
        }
        card.setLevel(cardLevel);
        card.setAmount(cardAmount);
    }

    public int getSize() {
        return this.cardCollection.size();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Card> getCardCollection() {
        return cardCollection;
    }

    public void setCardCollection(ArrayList<Card> cardCollection) {
        this.cardCollection = cardCollection;
    }

    public ArrayList<Integer> getBattleDeck() {
        return battleDeck;
    }

    public void setBattleDeck(ArrayList<Integer> battleDeck) {
        this.battleDeck = battleDeck;
    }

    public void swapBattleDeck(int cardInId, int cardOutID) {
        for (int i = 0; i < this.battleDeck.size(); i++) {
            if (battleDeck.get(i) == cardOutID) this.battleDeck.set(i, cardInId);
        }
    }
}
