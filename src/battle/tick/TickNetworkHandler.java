package battle.tick;

import battle.map.BattleMap;
import battle.common.Point;
import battle.newMap.*;
import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.ExtensionUtility;
import cmd.CmdDefine;
import cmd.receive.battle.spell.RequestDropSpell;
import cmd.receive.battle.tower.RequestChangeTowerStrategy;
import cmd.receive.battle.tower.RequestDestroyTower;
import cmd.receive.battle.tower.RequestPutTower;
import cmd.receive.battle.tower.RequestUpgradeTower;
import cmd.receive.battle.trap.RequestPutTrap;
import cmd.send.battle.opponent.*;
import cmd.send.battle.player.*;
import match.UserType;
import model.Inventory.Card;
import model.Inventory.Inventory;
import model.PlayerInfo;
import model.battle.Room;
import model.battle.RoomManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BattleHandler;

public class TickNetworkHandler {
    private final Logger logger = LoggerFactory.getLogger("TickHandler");

    public TickNetworkHandler() {

    }

    public void handleCommand(int tickNumber, PlayerInfo playerInfo, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.PUT_TOWER: {
                    System.out.println("[BattleHandler.java line 55] cmd Put tower: " + CmdDefine.PUT_TOWER);
                    RequestPutTower requestPutTower = new RequestPutTower(dataCmd);
                    processPutTower(tickNumber, playerInfo, requestPutTower);
                    break;
                }
                case CmdDefine.UPGRADE_TOWER: {
                    System.out.println("[BattleHandler.java line 56] cmd Upgrade tower: " + CmdDefine.UPGRADE_TOWER);
                    RequestUpgradeTower requestUpgradeTower = new RequestUpgradeTower(dataCmd);
                    processUpgradeTower(tickNumber, playerInfo, requestUpgradeTower);
                    break;
                }
                case CmdDefine.DROP_SPELL: {
                    System.out.println("[BattleHandler.java line 57] cmd Drop spell: " + CmdDefine.DROP_SPELL);
                    RequestDropSpell requestDropSpell = new RequestDropSpell(dataCmd);
                    processDropSpell(tickNumber, playerInfo, requestDropSpell);
                    break;
                }
                case CmdDefine.CHANGE_TOWER_STRATEGY: {
                    System.out.println("[BattleHandler.java line 58] cmd Change tower strategy: " + CmdDefine.CHANGE_TOWER_STRATEGY);
                    RequestChangeTowerStrategy requestChangeTowerStrategy = new RequestChangeTowerStrategy(dataCmd);
                    processChangeTowerStrategy(tickNumber, playerInfo, requestChangeTowerStrategy);
                    break;
                }
                case CmdDefine.DESTROY_TOWER: {
                    RequestDestroyTower requestDestroyTower = new RequestDestroyTower(dataCmd);
                    processDestroyTower(tickNumber, playerInfo, requestDestroyTower);
                    break;
                }
                case CmdDefine.PUT_TRAP: {
                    RequestPutTrap requestPutTrap = new RequestPutTrap(dataCmd);
                    processPutTrap(tickNumber, playerInfo, requestPutTrap);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processPutTower(int tickNumber, PlayerInfo playerInfo, RequestPutTower req) {
        System.out.println("BattleMap processPutTower");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            if (playerInfo.getUserType() == UserType.PLAYER) {
                User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo.getId());
                ExtensionUtility.getExtension().send(new ResponseRequestPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), 1, req.getTilePos(), tickNumber), user);
            }

            PlayerInfo opponentInfo = room.getOpponentPlayerByMyPlayerId(playerInfo.getId());
            if (opponentInfo.getUserType() == UserType.PLAYER) {
                User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentInfo.getId());
                ExtensionUtility.getExtension().send(new ResponseOppentPutTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTowerId(), 1, req.getTilePos(), tickNumber), opponent);
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processUpgradeTower(int tickNumber, PlayerInfo playerInfo, RequestUpgradeTower req) {
        System.out.println("BattleMap processPutTower");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            int towerId = req.getTowerId();
            BattleMap battleMap = room.getBattle().getBattleMapByPlayerId(playerInfo.getId());
            BattleMapObject battleMapObject = battleMap.battleMapObject;
            ObjectInTile obj = (battleMapObject.getCellObject(req.getTilePos()).getObjectInCell());

            User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo.getId());

            Tower tower = null;
            if (obj.getObjectInCellType() == ObjectInTileType.TOWER) {
                tower = (Tower) obj;
            }

            if (playerInfo.getUserType() == UserType.PLAYER) {
                if (tower == null) {
                    System.out.println("[BattleHandler.java line 103 processUpgradeTower]  tower null");
                    ExtensionUtility.getExtension().send(new ResponseRequestUpgradeTower(BattleHandler.BattleError.TOWER_NULL.getValue()), user);
                    return;
                }

                if (tower.getId() != towerId) {
                    System.out.println("[BattleHandler.java line 103 processUpgradeTower]  tower id not match");
                    ExtensionUtility.getExtension().send(new ResponseRequestUpgradeTower(BattleHandler.BattleError.TOWER_ID_NOT_MATCH.getValue()), user);
                    return;
                }

                System.out.println("[BattleHandler.java line 103 processUpgradeTower]  cellObject " + battleMapObject.getCellObject(req.getTilePos()));
                ExtensionUtility.getExtension().send(new ResponseRequestUpgradeTower(BattleHandler.BattleError.SUCCESS.getValue(),
                        req.getTowerId(), tower.getLevel() + 1, req.getTilePos(), tickNumber), user);
            }
//            Inventory inventory = (Inventory) Inventory.getModel(user.getId(), Inventory.class);
//            Card towerCard = inventory.getCardById(req.getTowerId());
//            if (towerCard.getCardRankNumber() < tower.getLevel()) {
//                tower = tower.upgradeTower();
//            } else {
//                return;
//            }

            int opponentId = room.getOpponentPlayerByMyPlayerId(playerInfo.getId()).getId();
            PlayerInfo opponentInfo = (PlayerInfo) PlayerInfo.getModel(opponentId, PlayerInfo.class);
            if (opponentInfo.getUserType() == UserType.PLAYER) {
                User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
                assert tower != null;
                ExtensionUtility.getExtension().send(new ResponseOpponentUpgradeTower(BattleHandler.BattleError.SUCCESS.getValue(),
                        req.getTowerId(), tower.getLevel() + 1, req.getTilePos(), tickNumber), opponent);
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processDropSpell(int tickNumber, PlayerInfo playerInfo, RequestDropSpell req) {
        System.out.println("requestDropSpellCmd");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            Inventory inventory = (Inventory) Inventory.getModel(playerInfo.getId(), Inventory.class);
            Point spellPos = req.getPixelPos();
            Card spellCard = inventory.getCardById(req.getSpellId());
            if (playerInfo.getUserType() == UserType.PLAYER) {
                User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo.getId());
                ExtensionUtility.getExtension().send(new ResponseRequestDropSpell(BattleHandler.BattleError.SUCCESS.getValue(),
                        req.getSpellId(), spellCard.getLevel(), spellPos, tickNumber), user);
            }


            int opponentId = room.getOpponentPlayerByMyPlayerId(playerInfo.getId()).getId();
            PlayerInfo opponentInfo = (PlayerInfo) PlayerInfo.getModel(opponentId, PlayerInfo.class);
            if (opponentInfo.getUserType() == UserType.PLAYER) {
                User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
                if (playerInfo.getUserType() == UserType.PLAYER) {
                    ExtensionUtility.getExtension().send(new ResponseOpponentDropSpell(BattleHandler.BattleError.SUCCESS.getValue(), req.getSpellId(), spellCard.getLevel(), spellPos, tickNumber), opponent);
                } else {
                    ExtensionUtility.getExtension().send(new ResponseOpponentDropSpell(BattleHandler.BattleError.SUCCESS.getValue(), req.getSpellId(), spellCard.getLevel(), spellPos.oppositePoint(), tickNumber), opponent);
                }
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processChangeTowerStrategy(int tickNumber, PlayerInfo playerInfo, RequestChangeTowerStrategy req) {
        System.out.println("BattleMap processChangeTowerStrategy");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());
            // TODO: implement tower entity in server
            User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo.getId());
            if (playerInfo.getUserType() == UserType.PLAYER) {
                ExtensionUtility.getExtension().send(new ResponseChangeTowerTargetStrategy(BattleHandler.BattleError.SUCCESS.getValue(),
                        req.getStrategyId(), req.getTilePos(), tickNumber), user);
            }

            int opponentId = room.getOpponentPlayerByMyPlayerId(playerInfo.getId()).getId();
            PlayerInfo opponentInfo = (PlayerInfo) PlayerInfo.getModel(opponentId, PlayerInfo.class);
            if (opponentInfo.getUserType() == UserType.PLAYER) {
                User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
                ExtensionUtility.getExtension().send(new ResponseOpponentChangeTowerTargetStrategy(BattleHandler.BattleError.SUCCESS.getValue(),
                        req.getStrategyId(), req.getTilePos(), tickNumber), opponent);
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processDestroyTower(int tickNumber, PlayerInfo playerInfo, RequestDestroyTower req) {
        System.out.println("BattleMap processDestroyTower");
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());

            // IMPORTANT: move this action to TickInternalHandler
//            BattleMap battleMap = room.getBattle().getBattleMapByPlayerId(user.getId());
//            BattleMapObject battleMapObject = battleMap.battleMapObject;
//            Tower tower = (Tower) battleMapObject.getCellObject(req.getTilePos()).getObjectInCell();
//            tower.destroyTower();
            User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo.getId());
            if (playerInfo.getUserType() == UserType.PLAYER) {
                ExtensionUtility.getExtension().send(new ResponseRequestDestroyTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTilePos(), tickNumber), user);
            }

            int opponentId = room.getOpponentPlayerByMyPlayerId(user.getId()).getId();
            PlayerInfo opponentInfo = (PlayerInfo) PlayerInfo.getModel(opponentId, PlayerInfo.class);
            if (opponentInfo.getUserType() == UserType.PLAYER) {
                User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
                ExtensionUtility.getExtension().send(new ResponseOpponentDestroyTower(BattleHandler.BattleError.SUCCESS.getValue(), req.getTilePos(), tickNumber), opponent);
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private void processPutTrap(int tickNumber, PlayerInfo playerInfo, RequestPutTrap req) {
        try {
            Room room = RoomManager.getInstance().getRoom(req.getRoomId());

            if (playerInfo.getUserType() == UserType.PLAYER) {
                User user = BitZeroServer.getInstance().getUserManager().getUserById(playerInfo.getId());
                ExtensionUtility.getExtension().send(new ResponseRequestPutTrap(BattleHandler.BattleError.SUCCESS.getValue(), req.getTilePos(), tickNumber), user);
            }

            int opponentId = room.getOpponentPlayerByMyPlayerId(playerInfo.getId()).getId();
            PlayerInfo opponentInfo = (PlayerInfo) PlayerInfo.getModel(opponentId, PlayerInfo.class);
            if (opponentInfo.getUserType() == UserType.PLAYER) {
                User opponent = BitZeroServer.getInstance().getUserManager().getUserById(opponentId);
                ExtensionUtility.getExtension().send(new ResponseOpponentPutTrap(BattleHandler.BattleError.SUCCESS.getValue(), req.getTilePos(), tickNumber), opponent);
            }
        } catch (Exception e) {
            logger.info("processGetName exception");
        }
    }
}
