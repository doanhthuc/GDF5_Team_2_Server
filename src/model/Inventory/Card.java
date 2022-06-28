package model.Inventory;

public class Card {
    protected int cardType;
    protected String decryption;
    protected int energy;
    protected int level;
    protected int accumulate;

    public Card() {
    }

    public Card(int Type, String decryption, int energy, int level, int accumulate) {
        this.cardType = Type;
        this.decryption = decryption;
        this.energy = energy;
        this.level = level;
        this.accumulate = accumulate;
    }
    public Card(int Type, int level, int accumulate) {
        this.cardType = Type;
        this.decryption = "";
        this.energy = 0;
        this.level = level;
        this.accumulate = accumulate;
    }

    public int getCardType() {
        return this.cardType;
    }

    public void increaseLevel() {
        this.level += 1;
    }

    public void increaseAccumulateCard(int amount) {
        this.accumulate += amount;
    }

    public void show() {
        System.out.println("Card" + this.cardType + " energy= " + this.energy + " level=" + this.level + " accumulate= " + this.accumulate);
    }

    public int getLevel() {
        return this.level;
    }

    public int getAmount() {
        return this.accumulate;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setAmount(int amount) {
        this.accumulate = amount;
    }
}
