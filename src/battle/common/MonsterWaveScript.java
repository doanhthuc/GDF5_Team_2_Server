package battle.common;

import java.util.List;

public class MonsterWaveScript {
    private List<MonsterWaveSlot> monsterWaveSlotList;

    public MonsterWaveScript(List<MonsterWaveSlot> monsterWaveSlotList) {
        this.monsterWaveSlotList = monsterWaveSlotList;
    }

    public List<MonsterWaveSlot> getMonsterWaveSlotList() {
        return monsterWaveSlotList;
    }

    public void setMonsterWaveSlotList(List<MonsterWaveSlot> monsterWaveSlotList) {
        this.monsterWaveSlotList = monsterWaveSlotList;
    }
}
