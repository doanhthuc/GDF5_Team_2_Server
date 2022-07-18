package battle.system;

import battle.common.UUIDGeneratorECS;

public class SystemECS implements Runnable {
    private final static String name = "SystemECS";
    private static int typeID;

    private long id;

    long currentMillis;
    long pastMillis;
    long tick;

    public SystemECS(int typeID) {
        typeID = typeID;
        id = UUIDGeneratorECS.genSystemID();
    }

    @Override
    public void run() {

    }

    public long getElapseTime() {
        this.currentMillis = java.lang.System.currentTimeMillis();
        this.tick = currentMillis - pastMillis;
        this.pastMillis = currentMillis;
        return this.tick;
    }
}
