//package test;
//
//import bitzero.engine.sessions.ISession;
//import bitzero.engine.sessions.Session;
//import bitzero.engine.sessions.SessionType;
//import bitzero.server.BitZeroServer;
//import bitzero.server.config.ConfigHandle;
//
//import bitzero.server.config.ServerSettings;
//import bitzero.server.entities.User;
//import bitzero.server.entities.managers.BZUserManager;
//import bitzero.server.extensions.IClientRequestHandler;
//import bitzero.server.extensions.data.DataCmd;
//import bitzero.server.util.ByteArray;
//import bitzero.util.ExtensionUtility;
//import bitzero.util.socialcontroller.bean.UserInfo;
//import cmd.receive.demo.ChangePosition;
//import extension.FresherExtension;
//import model.PlayerInfo;
//import org.apache.commons.lang.exception.ExceptionUtils;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import service.DemoHandler;
//import util.server.ServerConstant;
//
//import java.awt.*;
//import java.net.SocketAddress;
//import java.util.Iterator;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.*;
//import static test.TestConstant.CHANGE_POS;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class WorkshopTest {
//
//
//
//    /**
//     * Description:
//     * -
//     *
//     *
//     * Requirement:
//     * -
//     *
//     * Actions:
//     * -
//     */
//    @Test
//    @Order(5)
//    void validateUser() {
//
//        try{
//            FresherExtension extension = (FresherExtension)BitZeroServer.getInstance().getExtensionManager().getMainExtension();
//            UserInfo uInfo = extension.getUserInfo("username_" + TestConstant.USER_ID, TestConstant.USER_ID, "127.0.0.1");
//            assertEquals(TestConstant.USER_ID +"", uInfo.getUserId(),
//                    "\nChange setting conf/cluster.properties -> custom_login to : 2");
//        }catch(Exception e){
//            assertTrue(false,
//                    "\nError:\n"
//                    + ExceptionUtils.getStackTrace(e));
//        }
//
//    }
//
//
//    @Test
//    @Order(6)
//    void checkCacheInRAM(){
//
//        try{
//
//            FresherExtension extension = (FresherExtension)BitZeroServer.getInstance().getExtensionManager().getMainExtension();
//            int userId = TestConstant.USER_ID + 1;
//            ISession dmnSession = new Session();
//            dmnSession.setType(SessionType.VOID);
//
//            UserInfo uInfo = extension.getUserInfo("username_" + userId, userId, "127.0.0.1");
//
//            BitZeroServer.getInstance().getSessionManager().addSession(dmnSession);
//
//
//            User u = ExtensionUtility.instance().canLogin(uInfo, "", dmnSession);
//            //BitZeroServer.getInstance().getUserManager().addUser(u);
//
//            u.setProperty("userId", uInfo.getUserId());
//            Thread.sleep(500L);
//
//            User uNew = BitZeroServer.getInstance().getUserManager().getUserById(userId);
//            Object userInfo = uNew.getProperty(ServerConstant.PLAYER_INFO);
//            assertNotEquals(null, userInfo, TestConstant.ERROR_MSG_CACHE_IN_RAM);
//
//        }catch(Exception e){
//            assertTrue(false,
//                    "\nError:\n"
//                            + ExceptionUtils.getStackTrace(e));
//        }
//    }
//
//    @Test
//    @Order(0)
//    void initFramework() {
//
//        BitZeroServer localBitZeroServer = BitZeroServer.getInstance();
//        localBitZeroServer.setClustered(false);
//        localBitZeroServer.start();
//
//        try {
//            Thread.sleep(500L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    @Order(1)
//    void checkConfigDatabase(){
//        assertEquals("127.0.0.1:11211", ConfigHandle.instance().get("dservers"),
//                TestConstant.ERROR_MSG_DATABASE);
//
//    }
//
//    @Test
//    @Order(4)
//    void checkConfigListenedServer(){
//        for(Iterator iterator = BitZeroServer.getInstance().getConfigurator().getServerSettings().socketAddresses.iterator(); iterator.hasNext();)
//        {
//            bitzero.server.config.ServerSettings.SocketAddress addr = (ServerSettings.SocketAddress)iterator.next();
//
//            assertEquals(TestConstant.SERVER_ADDR, addr.address, TestConstant.ERROR_MSG_LISTEN_ADDR);
//            assertEquals(TestConstant.SERVER_PORT, addr.port, TestConstant.ERROR_MSG_LISTEN_PORT);
//
//        }
//
//    }
//
//    @Test
//    @Order(7)
//    void checkReadPacketChangePosition(){
//        try{
//            ByteArray byteArray = new ByteArray();
//            byteArray.writeInt(CHANGE_POS.x);
//            byteArray.writeInt(CHANGE_POS.y);
//
//            DataCmd dataCmd = new DataCmd(byteArray.getBytes());
//            ChangePosition pos = new ChangePosition(dataCmd);
//
//            assertEquals(CHANGE_POS.x, pos.x, TestConstant.ERROR_MSG_REQUEST_CHANGE_POS);
//            assertEquals(CHANGE_POS.y, pos.y, TestConstant.ERROR_MSG_REQUEST_CHANGE_POS);
//
//        }catch(Exception e){
//            assertTrue(false,
//                    "\nException:\n"
//                            + ExceptionUtils.getStackTrace(e));
//        }
//
//    }
//
//
//    void checkResponse(){
//        try{
//            ResponseChangePosition res = new ResponseChangePosition(DemoHandler.DemoError.SUCCESS.getValue(), new Point(CHANGE_POS.x, CHANGE_POS.y));
//            ByteArray byteArray = new ByteArray();
//            byteArray.writeByte(DemoHandler.DemoError.SUCCESS.getValue());
//            byteArray.writeInt(CHANGE_POS.x);
//            byteArray.writeInt(CHANGE_POS.y);
//
//            byte[] res_data = res.createData();
//
//            assertEquals(byteArray.getBytes().length, res_data.length,TestConstant.ERROR_MSG_CHANGE_POS_RES);
//            for (int i=0; i<byteArray.getBytes().length; i++){
//                assertEquals(byteArray.getBytes()[i], res_data[i], TestConstant.ERROR_MSG_CHANGE_POS_RES);
//            }
//        }catch(Exception e){
//            assertTrue(false,
//                    "\nException:\n"
//                            + ExceptionUtils.getStackTrace(e));
//        }
//    }
//
//    @Test
//    @Order(10)
//    void checkSaveInDB(){
//
////        try{
////            ByteArray byteArray = new ByteArray();
////            byteArray.writeInt(CHANGE_POS.x);
////            byteArray.writeInt(CHANGE_POS.y);
////
////            DataCmd dataCmd = new DataCmd(byteArray.getBytes());
////            ChangePosition pos = new ChangePosition(dataCmd);
////
////            int userId = getRandomNumber(10,10000);
////            ISession dmnSession = new Session();
////            dmnSession.setType(SessionType.VOID);
////
////            FresherExtension extension = (FresherExtension)BitZeroServer.getInstance().getExtensionManager().getMainExtension();
////            UserInfo uInfo = extension.getUserInfo("username_" + userId, userId, "127.0.0.1");
////            BZUserManager bzUserManager = (BZUserManager)BitZeroServer.getInstance().getUserManager();
////
////            User u = ExtensionUtility.instance().canLogin(uInfo, "", dmnSession);
////            //BitZeroServer.getInstance().getUserManager().addUser(u);
////
////            u.setProperty("userId", uInfo.getUserId());
////            PlayerInfo inf = new PlayerInfo(u.getId(), "username_" + u.getId());
////            u.setProperty(ServerConstant.PLAYER_INFO, inf);
////
////            DemoHandler handler = new DemoHandler();
////
////            try{
////                handler.processChangePosition(u, pos);
////            }catch(Exception ef){
////
////            }
////
////            Thread.sleep(500L);
////
////            PlayerInfo pInfo = null;
////            try{
////                pInfo = (PlayerInfo) PlayerInfo.getModel(u.getId(), PlayerInfo.class);
////            }catch(Exception e){
////                assertNotEquals(null, pInfo, TestConstant.ERROR_MSG_SAVE_DB);
////            }
////
////            assertNotEquals(null, pInfo, TestConstant.ERROR_MSG_SAVE_DB);
////
////            if (pInfo!=null){
////                for (int i=0; i<pInfo.getVisitedMap().length; i++){
////                    for (int j=0; j<pInfo.getVisitedMap()[i].length; j++){
////                        if ((i==CHANGE_POS.x && j==CHANGE_POS.y) || (i==0 && j==0)){
////                            assertEquals(true, pInfo.getVisitedMap()[i][j], TestConstant.ERROR_MSG_SAVE_DB);
////                        }
////                        else{
////                            System.out.println(" i = " + i + " , j = " + j);
////                            assertEquals(false, pInfo.getVisitedMap()[i][j], TestConstant.ERROR_MSG_SAVE_DB);
////                        }
////                    }
////                }
////            }
////
////        }catch(Exception e){
////
////        }
//
//
//    }
//
////    @Test
////    @Order(11)
////    void checkDispatchEvent(){
////        assertTrue(false);
////
////    }
//
//    public int getRandomNumber(int min, int max) {
//        return (int) ((Math.random() * (max - min)) + min);
//    }
//
//}
