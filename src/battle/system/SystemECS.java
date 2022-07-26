package battle.system;

import battle.common.UUIDGeneratorECS;

public class SystemECS implements Runnable {
    private final static String name = "SystemECS";
    private static int typeID;
    long currentMillis;
    long pastMillis;
    double tick;
    private long id;

    public SystemECS(int typeID) {
        SystemECS.typeID = typeID;
        id = UUIDGeneratorECS.genSystemID();
    }

    @Override
    public void run() {

    }

    public double getElapseTime() {
        this.currentMillis = java.lang.System.currentTimeMillis();
        this.tick = (double) currentMillis - pastMillis;
        this.pastMillis = currentMillis;
        return this.tick;
    }
}
