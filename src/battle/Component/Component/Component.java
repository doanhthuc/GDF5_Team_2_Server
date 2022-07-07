package battle.Component;

import battle.Common.Utils;
import battle.Config.GameConfig;

import java.awt.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;

public class Component {
    public int typeID = 0;
    public String name = "ComponentECS";
    public int id;
    public boolean _active;

    public Component() {
    }

    public Component(int typeID) {
        this.typeID = typeID;
        this.id=Utils.UUID.genIncrementID();
        this._active = true;
    }

    public boolean getActive() {
        return this._active;
    }

    public void setActive(boolean active) {
        this._active = active;
    }
}

class PositionComponent extends Component {
    public String name = "PositionComponent";

    public PositionComponent(int x, int y) {
        super(GameConfig.COMPONENT_ID.POSITION);
    }

    ;
}

class VelocityComponent extends Component {
    public String name = "VelocityComponent";
    public double speedX;
    public double speedY;
    public double originSpeedX;
    public double originSpeedY;
    public double dynamicPosition;
    public double originSpeed;

    public VelocityComponent(double speedX, double speedY, int dynamicPosition) {
        super(GameConfig.COMPONENT_ID.VELOCITY);
        this.speedX = speedX;
        this.speedY = speedY;
        this.dynamicPosition = dynamicPosition;
        this.originSpeed = Math.sqrt(this.speedX * this.speedX + this.speedY * this.speedY);
        this.originSpeedX=this.speedX;
        this.originSpeedY=this.speedY;
    }
    public double calulateSpeed(double speedX, double speedY)
    {
        return Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
    }
}

class PathComponent extends Component{
    public String name="PathComponent";
    public ArrayList<Point> path;
    public int currentPathIDx;
    public PathComponent(ArrayList<Point> path){
        super(GameConfig.COMPONENT_ID.PATH);
        this.path=path;
        this.currentPathIDx=0;
    }
}
class CollisionComponent extends Component{
    public String name="PathComponent";
    public int width,height;
    public CollisionComponent(int width,int height){
        super(GameConfig.COMPONENT_ID.COLLISION);
        this.width=width;
        this.height=height;
    }
}
class AttackComponent extends Component {
    public String name= " AttackComponent";
    int originDamage;
    int _damage;
    int targetStategy;
    int range;
    int originSpeed;
    int speed;
    int countdown;
    ArrayList<EffectComponent> effects= new ArrayList<EffectComponent>();
    public AttackComponent(int damage, int targetStrategy, int range, int speed, int countdown, EffectComponent effects){
        super(GameConfig.COMPONENT_ID.ATTACK);
        this.originDamage=damage;
        this._damage=damage;
        this.targetStategy=targetStrategy;
        this.range=range;
        this.speed=speed;
        this.countdown=countdown;
        this.effects.add(new DamageEffect(this._damage));
    }
    public void setDamage(int damage)
    {
        this._damage=damage;
        for(int i=0;i<this.effects.size();i++){
            DamageEffect effect= (DamageEffect) this.effects.get(i);
            if (effect.typeID==GameConfig.COMPONENT_ID.DAMAGE_EFFECT) {
                effect.damage=this._damage;
            }
        }
    }
    public double getDamage(){
        return this._damage;
    }
}