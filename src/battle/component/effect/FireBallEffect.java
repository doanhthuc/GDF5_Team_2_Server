package battle.component.effect;

import battle.common.Point;
import battle.component.common.Component;
import battle.config.GameConfig;

public class FireBallEffect extends Component {
    private String name = "FireBallEffect";
    public static int typeID = GameConfig.COMPONENT_ID.FIRE_BALL_EFFECT;
    private double a;
    private double accTime;
    private double maxDuration;
    private Point startPos;
    private Point endPos;
    private double v0;

    public FireBallEffect(double a, double maxDuration, Point startPos, Point endPos, double v0) {
        super(GameConfig.COMPONENT_ID.FIRE_BALL_EFFECT);
        this.reset(a, maxDuration, startPos, endPos, v0);
    }

    public void reset(double a, double maxDuration, Point startPos, Point endPos, double v0) {
        this.a = a;
        this.accTime = 0;
        this.maxDuration = maxDuration;
        this.startPos = startPos;
        this.endPos = endPos;
        this.v0 = v0;
    }

}
