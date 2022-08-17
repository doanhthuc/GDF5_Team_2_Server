package battle.tick;

import battle.Battle;
import battle.map.BattleMap;
import battle.common.EntityMode;
import battle.config.GameConfig;
import battle.newMap.BattleMapObject;
import battle.newMap.TileObject;
import battle.newMap.Tower;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.battle.spell.RequestDropSpell;
import cmd.receive.battle.tower.RequestChangeTowerStrategy;
import cmd.receive.battle.tower.RequestDestroyTower;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.receive.battle.tower.RequestUpgradeTower;
import cmd.receive.battle.trap.RequestPutTrap;
import model.PlayerInfo;
import model.battle.Room;
import model.battle.RoomManager;

public class TickInternalHandler {
    public TickInternalHandler() {
    }

    public void handleCommand(PlayerInfo playerInfo, DataCmd dataCmd) throws Exception {
        switch (dataCmd.getId()) {
            case CmdDefine.PUT_TOWER: {

                RequestPutTower req = new RequestPutTower(dataCmd);
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());

                EntityMode entityMode = room.getBattle().getEntityModeByPlayerID(playerInfo.getId());
                room.getBattle().buildTowerByTowerID(req.getTowerId(), req.getTilePos().x, req.getTilePos().y, entityMode);

                System.out.println("AAAAAAAAAAAA Handle put tower internal");
                break;
            }
            case CmdDefine.DROP_SPELL: {
                RequestDropSpell req = new RequestDropSpell(dataCmd);
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());
                EntityMode entityMode = room.getBattle().getEntityModeByPlayerID(playerInfo.getId());
                room.getBattle().castSpellBySpellID(req.getSpellId(), req.getPixelPos().x, req.getPixelPos().y, entityMode);
                System.out.println("AAAAAAAAAAAA Handle dropSpell internal");
                break;
            }
            case CmdDefine.PUT_TRAP:{
                RequestPutTrap req = new RequestPutTrap(dataCmd);
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());
                EntityMode entityMode = room.getBattle().getEntityModeByPlayerID(playerInfo.getId());
                room.getBattle().castSpellBySpellID(GameConfig.ENTITY_ID.TRAP_SPELL,req.getTilePos().x,req.getTilePos().y,entityMode);
                System.out.println("AAAAAAAAAAAA Handle dropTrap internal");
                break;
            }
            case CmdDefine.UPGRADE_TOWER:{
                System.out.println("AAAAAAAAAAAA Handle upgradeTower internal");
                RequestUpgradeTower req = new RequestUpgradeTower(dataCmd);
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());
                Battle battle = room.getBattle();
                Tower tower = getTowerByTilePosAndUser(battle, req.getTilePos().x, req.getTilePos().y, playerInfo);
                tower.upgradeTower();
                room.getBattle().handleUpgradeTower(tower.getEntityId(), tower.getLevel());
                break;
            }
            case CmdDefine.DESTROY_TOWER: {
                System.out.println("AAAAAAAAAAAA Handle destroyTower internal");
                RequestDestroyTower req = new RequestDestroyTower(dataCmd);
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());
                Battle battle = room.getBattle();
                int tilePosX = req.getTilePos().x;
                int tilePosY = req.getTilePos().y;
                Tower tower = getTowerByTilePosAndUser(battle, tilePosX, tilePosY, playerInfo);
                BattleMap battleMap = battle.getBattleMapByPlayerId(playerInfo.getId());
                TileObject tileObject = battleMap.battleMapObject.getCellObject(req.getTilePos());
                tileObject.destroyTower();
                battleMap.map[tilePosX][tilePosY] = GameConfig.MAP.NONE;
                battle.handleDestroyTower(tower.getEntityId());
                break;
            }
            case CmdDefine.CHANGE_TOWER_STRATEGY: {
                RequestChangeTowerStrategy req = new RequestChangeTowerStrategy(dataCmd);
                System.out.println("AAAAAAAAAAAA Handle changeTowerStrategy internal");
                Room room = RoomManager.getInstance().getRoom(req.getRoomId());
                Battle battle = room.getBattle();
                Tower tower = getTowerByTilePosAndUser(battle, req.getTilePos().x, req.getTilePos().y, playerInfo);
                battle.handleTowerChangeTargetStrategy((int) tower.getEntityId(), req.getStrategyId());
                break;
            }
            case CmdDefine.BORN_MONSTER:{
                Room room = RoomManager.getInstance().getRoom(dataCmd.readInt());
                int monsterTypeId = dataCmd.readInt();
                room.getBattle().bornMonsterByMonsterID(monsterTypeId,EntityMode.PLAYER);
                room.getBattle().bornMonsterByMonsterID(monsterTypeId,EntityMode.OPPONENT);
            }
        }
    }

    private Tower getTowerByTilePosAndUser(Battle battle, int x, int y, PlayerInfo playerInfo) {
        EntityMode entityMode = battle.getEntityModeByPlayerID(playerInfo.getId());
        BattleMap battleMap = battle.getBattleMapByEntityMode(entityMode);
        BattleMapObject battleMapObject = battleMap.battleMapObject;
        return (Tower) battleMapObject.getCellObject(x, y).getObjectInCell();
    }
}
