package cmd.send.battle.player;

import battle.newMap.*;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseRequestCellObject extends BaseMsg {
    private final short _error;
    private final TileObject tileObject;

    public ResponseRequestCellObject(short _error, TileObject tileObject) {
        super(CmdDefine.GET_CELL_OBJECT);
        this._error = _error;
        this.tileObject = tileObject;
        createData();
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
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
        return packBuffer(bf);
    }
}
