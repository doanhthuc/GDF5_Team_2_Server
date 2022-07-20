package battle.component.common;

import battle.common.EntityMode;
import battle.common.Point;
import battle.config.GameConfig;
import battle.factory.ComponentFactory;

import java.util.List;

public class PathComponent extends Component {
    private String name = "PathComponent";
    private List<Point> path;
    private int currentPathIDx;
    private EntityMode mode;

    public PathComponent(List<Point> pathTile, EntityMode mode, boolean isConvert) {
        super(GameConfig.COMPONENT_ID.PATH);
        this.reset(pathTile, mode, isConvert);
    }

    public void reset(List<Point> pathTile, EntityMode mode, boolean isConvert) {
        if (isConvert == true) {
          //  List<Point> pathTile2 = Utils.tileArray2PixelCellArray(pathTile, mode);
            this.path = pathTile;
        } else {
            this.path = pathTile;
        }
        this.mode = mode;
        this.currentPathIDx = 0;
    }

    public PathComponent clone() {
        try {
            return ComponentFactory.getInstance().createPathComponent(this.path, this.mode, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public List<Point> getPath() {
        return path;
    }

    public void setPath(List<Point> path) {
        this.path = path;
    }

    public void setCurrentPathIDx(int currentPathIDx) {
        this.currentPathIDx = currentPathIDx;
    }

}