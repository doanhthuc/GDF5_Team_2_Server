package battle.component.common;

import battle.common.EntityMode;
import battle.common.Point;
import battle.common.Utils;
import battle.config.GameConfig;

import java.nio.ByteBuffer;
import java.util.List;

public class PathComponent extends Component {
    private String name = "PathComponent";
    public static int typeID = GameConfig.COMPONENT_ID.PATH;
    private List<Point> path;
    private int currentPathIDx;
    private EntityMode mode;

    public PathComponent(List<Point> pathTile, EntityMode mode, boolean isConvert) {
        super(GameConfig.COMPONENT_ID.PATH);
        this.reset(pathTile, mode, isConvert);
    }

    public void reset(List<Point> pathTile, EntityMode mode, boolean isConvert) {
        if (isConvert == true) {
            // FIX ME: Code TileArray2PixelArray
            List<Point> pathTile2 = Utils.tileArray2PixelCellArray(pathTile, mode);
            this.path = pathTile2;
        } else {
            this.path = pathTile;
        }
        this.mode = mode;
        this.currentPathIDx = 0;
    }

//    public PathComponent clone() {
//        try {
//            return ComponentFactory.getInstance().createPathComponent(this.path, this.mode, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<Point> getPath() {
        return path;
    }

    public void setPath(List<Point> path) {
        this.path = path;
    }

    public void setCurrentPathIDx(int currentPathIDx) {
        this.currentPathIDx = currentPathIDx;
    }

    public int getCurrentPathIDx() {
        return currentPathIDx;
    }

    @Override
    public void createData(ByteBuffer bf) {
        short modeShort = Utils.getInstance().convertMode2Short(mode);
        super.createData(bf);

        bf.putShort(modeShort);
        bf.putInt(currentPathIDx);
        bf.putInt(path.size());
        for (Point p : path) {
            //TODO : Use Position as Int
            bf.putDouble(p.x);
            bf.putDouble(p.y);
        }
    }
}