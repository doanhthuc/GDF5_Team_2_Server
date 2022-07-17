package cmd.send.battle;

import battle.newMap.BattleMapObject;
import battle.newMap.CellObject;
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
                CellObject cellObject = battleMapObject.getCellObject(i, j);
                packCellPacket(cellObject, bf);
            }
        }
    }

    public void packCellPacket(CellObject cellObject, ByteBuffer bf) {
        bf.putInt(cellObject.getTilePos().x);
        bf.putInt(cellObject.getTilePos().y);
        bf.putInt(cellObject.getBuffCellType().value);
        bf.putInt(cellObject.getObjectInCell().getObjectInCellType().value);
        switch (cellObject.getObjectInCell().getObjectInCellType()) {
            case TOWER:
                Tower tower = (Tower) cellObject.getObjectInCell();
                bf.putInt(tower.getId());
                bf.putInt(tower.getLevel());
                break;
            case TREE:
                Tree tree = (Tree) cellObject.getObjectInCell();
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
