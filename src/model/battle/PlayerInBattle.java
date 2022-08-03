package model.battle;

import battle.Battle;
import model.Inventory.Card;
import model.Inventory.Inventory;
import model.PlayerInfo;

import java.util.List;

public class PlayerInBattle extends PlayerInfo {

    private int energyHouse;
    private int currentEnergy;
    private int maxEnergy = 30;
    private List<Card> battleDeck;

    public PlayerInBattle(PlayerInfo player) {
        super(player.getId(), player.getUserName(), player.getGold(), player.getGem(), player.getTrophy());
        setBattleDeck();
    }

    public void setBattleDeck() {
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

    public List<Card> getBattleDeck() {
        return battleDeck;
    }

}
