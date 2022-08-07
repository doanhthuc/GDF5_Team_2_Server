package battle.config.GameStat;

import java.util.HashMap;
import java.util.List;

public class TargetBuffStat {
    String name;
    String durationType;
    double durationUseCardLevel;
    HashMap<Integer,Double> duration;
    HashMap<Integer, List<EffectStat> > listEffect;
    String state;
    class EffectStat{
        String name;
        int type;
        double value;
    }
}
