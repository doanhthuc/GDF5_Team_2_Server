package model.Lobby;

import model.Item.Item;

import java.util.ArrayList;

public class LobbyDTO {
    public int chestId;
    public int state;
    public long claimTime;
    public ArrayList<Item> reward;
    public int gemChange;

    public LobbyDTO(int chestId, int state, long claimTime) {
        this.chestId = chestId;
        this.state = state;
        this.claimTime = claimTime;
    }

    public LobbyDTO(int chestId, int state, ArrayList<Item> reward, int gemcChange) {
        this.chestId = chestId;
        this.state = state;
        this.reward = reward;
        this.gemChange = gemChange;
    }

    public int getChestId() {
        return this.chestId;
    }

    public int getState() {
        return this.state;
    }

    public long getclaimTime() {
        return this.claimTime;
    }

    public ArrayList<Item> getReward() {
        return this.reward;
    }

    public int getGemchange() {
        return gemChange;
    }
}