package cmd.send.battle;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;
import java.nio.ByteBuffer;

public class ResponseEndBattle extends BaseMsg {
    private final short _error;
    private int battleResult;
    private int currentTrophy;
    private boolean haveChest;
    private int delaTrophy;
    private int userHP;
    private int opponentHP;

    public ResponseEndBattle(short _error, int battleResult, int userHP, int opponentHP, int currentTrophy, int deltaTrophy, boolean haveChest) {
        super(CmdDefine.END_BATTLE, _error);
        this.battleResult = battleResult;
        this.currentTrophy = currentTrophy;
        this.delaTrophy = deltaTrophy;
        this.haveChest= haveChest;
        this.userHP = userHP;
        this.opponentHP = opponentHP;
        this._error = _error;

    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        putStr(bf, this.battleResult);
        return packBuffer(bf);
    }
}
