package model.battle;

import model.Inventory.Card;
import model.Inventory.Inventory;
import model.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class PlayerInBattle extends PlayerInfo {

    private int energyHouse;
    private int currentEnergy;
    private int maxEnergy = 30;
    private int userType = 0;
    private int currentCard = 0; //BotOnly
    private int cardDeckSize = 4;
    private List<Card> battleDeck;

    public PlayerInBattle(PlayerInfo player) {
        super(player.getId(), player.getUserName(), player.getGold(), player.getGem(), player.getTrophy());
        this.setUserType(player.getUserType());
        setBattleDeck();
    }

    public void setBattleDeck() {
        this.battleDeck = new ArrayList<>();
        try {
            Inventory inventory = (Inventory) Inventory.getModel(this.getId(), Inventory.class);
            List<Card> cardListInCollection = inventory.getCardCollection();
            List<Integer> cardIdListInBattleDeck = inventory.getBattleDeck();
            cardIdListInBattleDeck.forEach(cardId -> {
                cardListInCollection.forEach(card -> {
                    if (card.getCardType() == cardId) {
                        this.battleDeck.add(card);
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCurrentCard() {
        return this.currentCard;
    }

    public void increaseCurrentCard() {
        this.currentCard++;
        this.currentCard %= this.battleDeck.size();
    }

    public int getCardDeckSize() {
        return this.cardDeckSize;
    }

    public List<Card> getBattleDeck() {
        return battleDeck;
    }

    public void moveCardToEnd(int cardToUseID) {
        Card cardToUse = this.battleDeck.get(cardToUseID);
        this.battleDeck.add(cardToUse);
        this.battleDeck.remove(cardToUseID);
    }

}
