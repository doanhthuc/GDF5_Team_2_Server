package model.battle;

import model.Inventory.Card;
import model.Inventory.Inventory;
import model.PlayerInfo;

import java.util.List;

public class PlayerInBattle extends PlayerInfo {

    private List<Card> battleDeck;

    public PlayerInBattle(PlayerInfo player) {
        super(player.getId(), player.getUserName(), player.getGold(), player.getGem(), player.getTrophy());
        setBattleDeck();
    }

    public void setBattleDeck() {
        try {
            Inventory battleDeckList = (Inventory) Inventory.getModel(this.getId(), Inventory.class);
            this.battleDeck = battleDeckList.getCardCollection().subList(0, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Card> getBattleDeck() {
        return battleDeck;
    }

}
