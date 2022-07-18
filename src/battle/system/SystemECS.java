package battle.system;

public class System implements Runnable{
    private final static String name="SystemECS";
    private static long typeID;

    private long id;

    long currentMillis;
    long pastMillis;
    long tick;
    @Override
    public void run() {

    }
    public long getEclapseTime(){
        this.currentMillis = java.lang.System.currentTimeMillis();
        this.tick = currentMillis - pastMillis;
        this.pastMillis = currentMillis;
        return this.tick;
    }
}
