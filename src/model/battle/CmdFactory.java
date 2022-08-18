package model.battle;

import bitzero.server.exceptions.BZException;
import bitzero.server.extensions.data.DataCmd;
import bitzero.server.util.ByteArray;
import cmd.CmdDefine;

public class CmdFactory {
    public static DataCmd createRequestPutTower(int roomID, int towerID, int tilePosX, int tilePosY) throws BZException {
        ByteArray dataCmdBody = new ByteArray();
        dataCmdBody.writeInt(roomID);
        dataCmdBody.writeInt(towerID);
        dataCmdBody.writeInt(tilePosX);
        dataCmdBody.writeInt(tilePosY);
        System.out.println("handleBotAction");
        DataCmd reqPutTowerDataCmd = new DataCmd(dataCmdBody.getBytes());
        reqPutTowerDataCmd.setId(CmdDefine.PUT_TOWER);
        return reqPutTowerDataCmd;
    }

    public static DataCmd createRequestDropSpell(int roomID, int spellID, double tilePosX, double tilePosY) throws BZException {
        ByteArray dataCmdBody = new ByteArray();
        dataCmdBody.writeInt(roomID);
        dataCmdBody.writeInt(spellID);
        dataCmdBody.writeDouble(tilePosX);
        dataCmdBody.writeDouble(tilePosY);
        System.out.println("handleBotAction");
        DataCmd reqDropSpell = new DataCmd(dataCmdBody.getBytes());
        reqDropSpell.setId(CmdDefine.DROP_SPELL);
        return reqDropSpell;
    }

    public static DataCmd createRequestUpgradeTower(int roomID, int towerID, int tilePosX, int tilePosY) throws BZException {
        ByteArray dataCmdBody = new ByteArray();
        dataCmdBody.writeInt(roomID);
        dataCmdBody.writeInt(towerID);
        dataCmdBody.writeInt(tilePosX);
        dataCmdBody.writeInt(tilePosY);
        System.out.println("handleBotAction");
        DataCmd reqUpgradeTowerDataCmd = new DataCmd(dataCmdBody.getBytes());
        reqUpgradeTowerDataCmd.setId(CmdDefine.UPGRADE_TOWER);
        return reqUpgradeTowerDataCmd;
    }

    public static DataCmd createBornMonsterCmd(int roomID, int monsterID) throws BZException {
        ByteArray dataCmdBody = new ByteArray();
        dataCmdBody.writeInt(roomID);
        dataCmdBody.writeInt(monsterID);
        DataCmd bornMonsterCmd = new DataCmd(dataCmdBody.getBytes());
        bornMonsterCmd.setId(CmdDefine.BORN_MONSTER);
        return bornMonsterCmd;
    }

}
