package battle.Component.EffectComponent;
import battle.Config.GameConfig;

public class BuffAttackDamageEffect extends EffectComponent {
    private String name = "BuffAttackDamageEffect";
    private double percent;

    public BuffAttackDamageEffect(double percent) {
        super(GameConfig.COMPONENT_ID.BUFF_ATTACK_DAMAGE);
        this.percent = percent;
    }

    public BuffAttackDamageEffect clone() {
        return new BuffAttackDamageEffect(this.percent);
    }

    public void reset() {

    };

}