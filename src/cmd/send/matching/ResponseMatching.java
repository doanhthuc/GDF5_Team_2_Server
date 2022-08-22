package cmd.send.matching;

import battle.common.EntityMode;
import battle.common.Utils;
import battle.map.BattleMap;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import cmd.obj.matching.OpponentInfo;

import java.nio.ByteBuffer;

public class ResponseMatching extends BaseMsg {
    public short error;
    private int roomId;
    private EntityMode entityMode;
    public BattleMap playerMap;
    public BattleMap opponentMap;
    public OpponentInfo opponentInfo;

    public ResponseMatching(short error, int roomId, EntityMode entityMode, BattleMap playerMap, BattleMap opponentMap, OpponentInfo opponentInfo) {
        super(CmdDefine.MATCHING);
        this.error = error;
        this.roomId = roomId;
        this.entityMode = entityMode;
        this.playerMap = playerMap;
        this.opponentMap = opponentMap;
        this.opponentInfo = opponentInfo;

    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putShort(error);
        bf.putInt(roomId);
        bf.putShort(Utils.getInstance().convertMode2Short(entityMode));
        packMap(bf, this.playerMap);
        packMap(bf, this.opponentMap);
        bf.putInt(opponentInfo.getId());
        putStr(bf, opponentInfo.getUsername());
        bf.putInt(opponentInfo.getTrophy());
        return packBuffer(bf);
    }

    private void packMap(ByteBuffer bf, BattleMap battleMap) {
        bf.putInt(battleMap.mapH);
        bf.putInt(battleMap.mapW);

        for (int j = battleMap.mapH - 1; j >= 0; j--)
            for (int i = 0; i < battleMap.mapW; i++)
                bf.putInt(battleMap.map[i][j]);

        bf.putInt(battleMap.path.size());
        for (int i = battleMap.path.size() - 1; i >= 0; i--) {
            bf.putInt(battleMap.path.get(i).x);
            bf.putInt(battleMap.path.get(i).y);
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
