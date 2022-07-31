package cmd.send.battle.player;

import battle.common.Point;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;
import java.util.List;

public class ResponseRequestGetBattleInfo extends BaseMsg {
    private short _error;
    private final long startTime;
    private final int waveAmount;
    private final List<List<Integer>> monsterWave;

    public ResponseRequestGetBattleInfo(short _error, long startTime, int waveAmount, List<List<Integer>> monsterWave) {
        super(CmdDefine.GET_BATTLE_INFO);
        this._error = _error;
        this.startTime = startTime;
        this.waveAmount = waveAmount;
        this.monsterWave = monsterWave;
    }

    @Override
    public byte[] createData() {
        System.out.println("SendMonsterWave");
        ByteBuffer bf = makeBuffer();
        bf.putLong(startTime);
        System.out.println(startTime);
        bf.putInt(waveAmount);
        for (int i = 0; i < waveAmount; i++) {
            bf.putInt(monsterWave.get(i).size());
            for (int j = 0; j < monsterWave.get(i).size(); j++) {
                bf.putInt(monsterWave.get(i).get(j));
         //       System.out.print(monsterWave.get(i).get(j)+" ");
            }
          //  System.out.println();
        }
        return packBuffer(bf);
    }
}
