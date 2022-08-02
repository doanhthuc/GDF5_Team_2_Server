package model;

import util.database.DataModel;

public class PlayerInfo extends DataModel {
    // Zing me
    private int id;
    private String username;
    private int trophy;
    private int gold;
    private int gem;
    private int botType;

    public PlayerInfo(int _id, String username, int gold, int gem, int trophy) {
        super();
        this.id = _id;
        this.username = username;
        this.gold = gold;
        this.gem = gem;
        this.trophy = trophy;
        this.botType = 0;
    }

    public String toString() {
        String res = String.format("%s|%s|%s|%s|%s", new Object[]{id, username, gold, gem, trophy});
        return res;
    }

    public String getUserName() {
        return username;
    }

    public int getGold() {
        return this.gold;
    }

    public int getGem() {
        return this.gem;
    }

    public int getTrophy() {
        return this.trophy;
    }

    public int getId() {
        return this.id;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void addGem(int gem) {
        this.gem += gem;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public void setTrophy(int trophy) {
        this.trophy = trophy;
    }

    public void show() {
        System.out.print("id=" + this.id);
        System.out.print(" username=" + this.username);
        System.out.print(" gold=" + this.gold);
        System.out.print(" gem=" + this.gem);
        System.out.println(" trophy=" + this.trophy);
    }

    public void setIsBot(int botType) {
        this.botType = botType;
    }

    public int getBotType() {
        return this.botType;
    }
}
