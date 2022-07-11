package battle.System;

import battle.Entity.EntityECS;

import java.util.ArrayList;

public class System implements Runnable{
    int id=0;
    String name="SystemECS";
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
