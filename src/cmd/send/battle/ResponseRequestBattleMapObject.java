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
    private final BattleMapObject battleMapObject;

    public ResponseRequestBattleMapObject(short _error, BattleMapObject battleMapObject) {
        super(CmdDefine.GET_BATTLE_MAP_OBJECT);
        this._error = _error;
        this.battleMapObject = battleMapObject;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(battleMapObject.getHeight());
        bf.putInt(battleMapObject.getWidth());
        System.out.println("ResponseRequestBattleMapObject: " + battleMapObject.getHeight() + " " + battleMapObject.getWidth());
        for (int i = 0; i < battleMapObject.getHeight(); i++) {
            for (int j = 0; j < battleMapObject.getWidth(); j++) {
                System.out.println();
                System.out.println("ResponseRequestBattleMapObject: " + i + " " + j);
                packCellPacket(i, j, bf);
            }
        }
        return packBuffer(bf);
    }

    public void packCellPacket(int x, int y, ByteBuffer bf) {
        CellObject cellObject = battleMapObject.getCellObject(x, y);
        System.out.println("[ResponseRequestBattleMapObject] cellObject: " + cellObject);
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
