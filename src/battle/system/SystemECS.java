package battle.system;

import battle.Battle;
import battle.common.UUIDGeneratorECS;

public class SystemECS {
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

    public void run(Battle battle) throws Exception {

    }

    public double getElapseTime() {
        this.currentMillis = java.lang.System.currentTimeMillis();
        this.tick = (double) currentMillis - pastMillis;
        this.pastMillis = currentMillis;
        return this.tick;
    }
}
