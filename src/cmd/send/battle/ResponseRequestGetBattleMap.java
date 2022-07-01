package cmd.send.battle;

import battle.BattleMap;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.Inventory.Card;
import model.PlayerInfo;

import java.nio.ByteBuffer;

public class ResponseRequestGetBattleMap extends BaseMsg {
    public BattleMap battleMap;
    public short error;

    public ResponseRequestGetBattleMap(short _error, BattleMap battleMap) {
        super(CmdDefine.GET_BATTLE_MAP);
        this.battleMap = battleMap;
        error = _error;
    }

    public ResponseRequestGetBattleMap(short _error) {
        super(CmdDefine.GET_BATTLE_MAP);
        error = _error;
    }


    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(battleMap.mapH);
        bf.putInt(battleMap.mapW);
            for (int j = 0; j < battleMap.mapH; j++)
                for (int i = 0; i < battleMap.mapW; i++)
                    bf.putInt(battleMap.map[i][j]);
        for (int i = 0; i < battleMap.path.size(); i++) {
            bf.putInt(battleMap.path.get(i).x);
            bf.putInt(battleMap.path.get(i).y);
        }
        return packBuffer(bf);
    }
}