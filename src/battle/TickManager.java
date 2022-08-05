package battle;

import battle.common.Pair;
import battle.config.GameConfig;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class TickManager {
    private int currentTick = 0;
    private int tickRate = GameConfig.BATTLE.TICK_RATE;
    private Map<Integer, Queue<Pair<User, DataCmd>>> inputTick = new HashMap<>();
    private TickNetworkHandler tickNetworkHandler = new TickNetworkHandler();
    private TickInternalHandler tickInternalHandler = new TickInternalHandler();
    private final long startTime;

    public TickManager (long startTime) {
        this.startTime = startTime;
    }

    public void addInput (Pair<User, DataCmd> input) {
        DataCmd dataCmd = input.second;
        int currentTick = this.getCurrentTick();

        switch (dataCmd.getId()) {
            case CmdDefine.PUT_TOWER: {
                int futureTick = currentTick + GameConfig.BATTLE.DELAY_BUILD_TOWER / this.tickRate;

                int currentTick2 = (int) ((System.currentTimeMillis() - this.startTime) / this.tickRate);
                System.out.println("xx Latest Tick = " + currentTick);
                System.out.println("xx Current Tick = " + currentTick2);
                System.out.println("xx Future Tick put tower = " + futureTick);
                Queue<Pair<User, DataCmd>> queue = this.inputTick.getOrDefault(futureTick, new LinkedList<>());
                queue.add(input);
                this.tickNetworkHandler.handleCommand(futureTick, input.first, input.second);
                break;
            }
            case CmdDefine.DROP_SPELL: {
                int futureTick = currentTick + 1;
                Queue<Pair<User, DataCmd>> queue = this.inputTick.getOrDefault(futureTick, new LinkedList<>());
                queue.add(input);
                this.tickNetworkHandler.handleCommand(futureTick, input.first, input.second);
                break;
            }
        }
    }

    public void handleInternalInputTick(int tickNumber) {
        Queue<Pair<User, DataCmd>> queue = this.inputTick.getOrDefault(tickNumber, new LinkedList<>());
        while (!queue.isEmpty()) {
            Pair<User, DataCmd> data = queue.poll();
            tickInternalHandler.handleCommand(data.first, data.second);
        }
    }

    public void increaseTick () {
        this.currentTick++;
    }

    public int getTickRate () {
        return this.tickRate;
    }

    public int getCurrentTick () {
        return this.currentTick;
    }
}
