package model.Lobby;

import model.Chest.Chest;
import model.Chest.ChestDefine;

public class LobbyChest extends Chest {
    private int state;
    private long claimTime = 0;

    public LobbyChest(int state) {
        this.state = state;
        this.chestType = ChestDefine.BRONZE_CHEST;
        this.cardSlot = ChestDefine.CARD_SLOT;
        if (this.state == LobbyChestDefine.CLAIMABLE_STATE) this.claimTime = System.currentTimeMillis();
    }

    public LobbyChest(int state, long remainingTime) {
        this.state = LobbyChestDefine.OPENING_STATE;
        this.chestType = ChestDefine.BRONZE_CHEST;
        this.cardSlot = ChestDefine.CARD_SLOT;
        this.claimTime = System.currentTimeMillis() + remainingTime;
    }

    public void unlock() {
        if (this.state == LobbyChestDefine.NOT_OPENING_STATE) {
            this.state = LobbyChestDefine.OPENING_STATE;
            this.claimTime = System.currentTimeMillis() + LobbyChestDefine.unlockTime;
        }
    }

    public void setState(int state) {
        this.state = state;
    }

    public void updateChest() {
        if ((this.state == LobbyChestDefine.OPENING_STATE)
                && (this.claimTime <= System.currentTimeMillis())) this.state = LobbyChestDefine.CLAIMABLE_STATE;
    }

    public long getRemainingTime() {
        return (this.claimTime - System.currentTimeMillis());
    }

    public long getClaimTime() {
        return this.claimTime;
    }

    public int getState() {
        return this.state;
    }
}
