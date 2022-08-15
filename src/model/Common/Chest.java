package model.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Chest {
    public int chestType;
    public ArrayList<Item> reward = new ArrayList<Item>();
    public int cardSlot;

    public Chest() {
        this.chestType = ChestDefine.BRONZE_CHEST;
        this.cardSlot = ChestDefine.CARD_SLOT;
    }

    public Chest(int chesttype, int cardSlot) {
        this.chestType = chesttype;
        this.cardSlot = cardSlot;
    }

    public Chest(Chest ch) {
        this.chestType = ch.chestType;
        this.cardSlot = ch.cardSlot;
    }

    public void addReward(Item i) {
        this.reward.add(i);
    }

    public void randomRewardItem() {
        Random random = new Random();
        while (this.reward.size() > 0) {
            this.reward.remove(0);
        }
        int goldQuantity = random.nextInt(ChestDefine.MAXGOLD - ChestDefine.MINGOLD) + ChestDefine.MINGOLD;
        Item GoldItem = new Item(ItemDefine.GOLDTYPE, goldQuantity);
        this.reward.add(GoldItem);
        List<Integer> range = IntStream.range(0, ItemDefine.CARDAMOUNT).boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(range);
        range = range.subList(0, this.cardSlot + 1);
        for (int i = 1; i <= this.cardSlot; i++) {
            int cardType = range.get(i - 1);
            int quantity = random.nextInt(ChestDefine.MAXCARD - ChestDefine.MINCARD) + ChestDefine.MINCARD;
            this.reward.add(new Item(cardType, quantity));
        }
    }

    public void showReward() {
        for (int i = 0; i < this.reward.size(); i++)
            this.reward.get(i).show();
    }

    public ArrayList<Item> getChestReward() {
        return this.reward;
    }
}
