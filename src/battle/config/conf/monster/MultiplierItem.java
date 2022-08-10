package battle.config.conf.monster;


import java.io.Serializable;

public class MultiplierItem implements Serializable {
    int hp;
    int numberMonsters;
    int levelTowers;

    public int getHp() {
        return hp;
    }

    public int getNumberMonsters() {
        return numberMonsters;
    }

    public int getLevelTowers() {
        return levelTowers;
    }
}
