package battle.component.Component;

import battle.common.Point;
import battle.config.GameConfig;

import java.util.List;

public class PathComponent extends Component {
    private String name = "PathComponent";
    private List<Point> path;
    private int currentPathIDx;

    public PathComponent(List<Point> path) {
        super(GameConfig.COMPONENT_ID.PATH);
        this.path = path;
        this.currentPathIDx = 0;
    }

    public List<Point> getPath() {
        return path;
    }

    public void setPath(List<Point> path) {
        this.path = path;
    }

    public int getCurrentPathIDx() {
        return currentPathIDx;
    }

    public void setCurrentPathIDx(int currentPathIDx) {
        this.currentPathIDx = currentPathIDx;
    }

    public void reset(List<Point> path) {
        this.path = path;
        this.currentPathIDx = 0;
    }

    @Override
    public String getName() {
        return name;
    }
}