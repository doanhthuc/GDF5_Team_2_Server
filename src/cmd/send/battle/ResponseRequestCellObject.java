package cmd.send.battle;

import battle.newMap.*;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

import static battle.newMap.ObjectInCellType.*;

public class ResponseRequestCellObject extends BaseMsg {
    private final short _error;
    private final CellObject cellObject;

    public ResponseRequestCellObject(short _error, CellObject cellObject) {
        super(CmdDefine.GET_CELL_OBJECT);
        this._error = _error;
        this.cellObject = cellObject;
        createData();
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
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
        return packBuffer(bf);
    }
}
