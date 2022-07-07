package cmd.send.matching;

import battle.BattleMap;
import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

public class ResponseMatching extends BaseMsg {
    public short error;
    public BattleMap playerMap;
    public BattleMap opponentMap;

    public ResponseMatching(short error, BattleMap playerMap, BattleMap opponentMap) {
        super(CmdDefine.MATCHING);
        this.error = error;
        this.playerMap = playerMap;
        this.opponentMap = opponentMap;
    }
}
