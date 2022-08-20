package model.battle;

import battle.config.GameConfig;
import match.UserType;
import model.Inventory.Card;
import model.Inventory.Inventory;
import model.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class PlayerInBattle extends PlayerInfo {

    private int playerHP;
    private int playerEnergy;
    private int maxEnergy = 30;
    private int userType = UserType.PLAYER;
    private int cardDeckSize = 4;
    private List<Card> battleDeck;

    public PlayerInBattle(PlayerInfo player) {
        super(player.getId(), player.getUserName(), player.getGold(), player.getGem(), player.getTrophy());
        this.setUserType(player.getUserType());
        this.initPlayerHpAndEnergy();
        setBattleDeck();
    }

    public void initPlayerHpAndEnergy() {
        this.playerHP = GameConfig.PLAYER_HP;
        this.playerEnergy = GameConfig.PLAYER_ENERGY;
    }

    public void minusPlayerHP(int hp) {
        this.playerHP = Math.max(0, this.playerHP - hp);
    }

    public void addPlayerEnergy(int energy) {
        this.playerEnergy = Math.min(this.maxEnergy, this.playerEnergy + energy);
    }

    public void minusPlayerEnergy(int energy) {
        this.playerEnergy = Math.max(0, this.playerEnergy - energy);
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

    public int getPlayerHP() {
        return playerHP;
    }

    public void setPlayerHP(int playerHP) {
        this.playerHP = playerHP;
    }

    public int getPlayerEnergy() {
        return playerEnergy;
    }

    public void setPlayerEnergy(int playerEnergy) {
        this.playerEnergy = playerEnergy;
    }
}
