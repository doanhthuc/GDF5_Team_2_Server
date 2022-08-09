package battle;

import battle.common.Pair;
import battle.config.GameConfig;
import bitzero.server.entities.User;
import bitzero.server.extensions.data.DataCmd;
import cmd.CmdDefine;
import cmd.receive.battle.tower.RequestChangeTowerStrategy;
import cmd.receive.battle.tower.RequestDestroyTower;

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

    public TickManager(long startTime) {
        this.startTime = startTime;
    }

    public void addInput(Pair<User, DataCmd> input) {
        DataCmd dataCmd = input.second;
        int currentTick = this.getCurrentTick();

        switch (dataCmd.getId()) {
            case CmdDefine.PUT_TOWER:
            case CmdDefine.UPGRADE_TOWER: {
                int futureTick = currentTick + GameConfig.BATTLE.DELAY_BUILD_TOWER / this.tickRate;
                Queue<Pair<User, DataCmd>> queue = this.getInputQueueOfTick(futureTick);
                queue.add(input);
                this.tickNetworkHandler.handleCommand(futureTick, input.first, input.second);
                break;
            }

            case CmdDefine.DESTROY_TOWER:
            case CmdDefine.CHANGE_TOWER_STRATEGY:
            case CmdDefine.PUT_TRAP: {
                int nextTick = currentTick + 1;

//                int currentTick2 = (int) ((System.currentTimeMillis() - this.startTime) / this.tickRate);
//                System.out.println("xx Latest Tick = " + currentTick);
//                System.out.println("xx Current Tick = " + currentTick2);
//                System.out.println("xx Future Tick upgrade tower = " + nextTick);

                Queue<Pair<User, DataCmd>> queue = this.getInputQueueOfTick(nextTick);
                queue.add(input);
                this.tickNetworkHandler.handleCommand(nextTick, input.first, input.second);
                break;
            }
            case CmdDefine.DROP_SPELL: {
                int futureTick = currentTick + 1;

                int currentTick2 = (int) ((System.currentTimeMillis() - this.startTime) / this.tickRate);
//                System.out.println("xx Latest Tick = " + currentTick);
//                System.out.println("xx Current Tick = " + currentTick2);
//                System.out.println("xx Future Tick put tower = " + futureTick);

                Queue<Pair<User, DataCmd>> queue = this.getInputQueueOfTick(futureTick);
                queue.add(input);
                this.inputTick.put(futureTick, queue);
                this.tickNetworkHandler.handleCommand(futureTick, input.first, input.second);
                break;
            }//                System.out.println("xx Future Tick put tower = " + futureTick);
//                this.inputTick.put(futureTick, queue);
        }
    }

    public void handleInternalInputTick(int tickNumber) throws Exception {
        Queue<Pair<User, DataCmd>> queue = this.inputTick.get(tickNumber);
        if (queue != null) System.out.println("queue Internal InputTick Size " + queue.size());
        while (queue != null && !queue.isEmpty()) {
            Pair<User, DataCmd> data = queue.poll();
            tickInternalHandler.
                    handleCommand(data.first, data.second);
        }
    }

    public void increaseTick() {
        this.currentTick++;
    }

    public int getTickRate() {
        return this.tickRate;
    }

    public int getCurrentTick() {
//        return this.currentTick;
        return (int) ((System.currentTimeMillis() - this.startTime) / this.tickRate);
    }

    private Queue<Pair<User, DataCmd>> getInputQueueOfTick(int tickNumber) {
        Queue<Pair<User, DataCmd>> queue = this.inputTick.get(tickNumber);
        if (queue == null) {
            queue = new LinkedList<>();
            this.inputTick.put(tickNumber, queue);
        }
        return queue;
    }
}
