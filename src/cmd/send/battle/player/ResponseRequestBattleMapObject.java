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
        packMapObjectPacket(myBattleMapObject, bf);
        packMapObjectPacket(opponentBattleMapObject, bf);
        return packBuffer(bf);
    }

    public void packMapObjectPacket(BattleMapObject battleMapObject, ByteBuffer bf) {
        bf.putInt(battleMapObject.getHeight());
        bf.putInt(battleMapObject.getWidth());
        for (int i = 0; i < battleMapObject.getHeight(); i++) {
            for (int j = 0; j < battleMapObject.getWidth(); j++) {
                TileObject tileObject = battleMapObject.getTileObject(i, j);
                packCellPacket(tileObject, bf);
            }
        }
    }

    public void packCellPacket(TileObject tileObject, ByteBuffer bf) {
        bf.putInt(tileObject.getTilePos().x);
        bf.putInt(tileObject.getTilePos().y);
        bf.putInt(tileObject.getBuffCellType().value);
        bf.putInt(tileObject.getObjectInTile().getObjectInCellType().value);
        switch (tileObject.getObjectInTile().getObjectInCellType()) {
            case TOWER:
                Tower tower = (Tower) tileObject.getObjectInTile();
                bf.putInt(tower.getId());
                bf.putInt(tower.getLevel());
                break;
            case TREE:
                Tree tree = (Tree) tileObject.getObjectInTile();
                bf.putDouble(tree.getHp());
                break;
            case PIT:
                bf.putInt(-1);
                break;
            default:
                break;
        }
    }
}
