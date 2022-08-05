package model.battle;

import battle.Battle;
import model.Inventory.Card;
import model.Inventory.Inventory;
import model.PlayerInfo;

import java.util.ArrayList;
import java.util.List;

public class PlayerInBattle extends PlayerInfo {

    private int energyHouse;
    private int currentEnergy;
    private int maxEnergy = 30;
    private int userType = 0;
    private List<Card> battleDeck;

    public PlayerInBattle(PlayerInfo player) {
        super(player.getId(), player.getUserName(), player.getGold(), player.getGem(), player.getTrophy());
        this.setUserType(player.getUserType());
//        setBattleDeck();
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

    public List<Card> getBattleDeck() {
        return battleDeck;
    }

}
