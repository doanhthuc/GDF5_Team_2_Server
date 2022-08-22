package cmd.send.battle.player;

import battle.newMap.BattleMapObject;
import battle.newMap.TileObject;
import battle.newMap.Tower;
import battle.newMap.Tree;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseRequestBattleMapObject extends BaseMsg {
    private final short _error;
    private final BattleMapObject myBattleMapObject;
    private final BattleMapObject opponentBattleMapObject;

    public ResponseRequestBattleMapObject(short _error, BattleMapObject myBattleMapObject, BattleMapObject opponentBattleMapObject) {
        super(CmdDefine.GET_BATTLE_MAP_OBJECT);
        this._error = _error;
        this.myBattleMapObject = myBattleMapObject;
        this.opponentBattleMapObject = opponentBattleMapObject;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        myBattleMapObject.createData(bf);
        opponentBattleMapObject.createData(bf);
        return packBuffer(bf);
    }
}
