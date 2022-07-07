class LifeComponent extends InfoComponent{
    public String name= "LifeComponent";
    double hp;
    double maxHP;
    public LifeComponent(double hp, double maxHP)
    {
        super(GameConfig.COMPONENT_ID.LIFE);
        this.hp=hp;
        if (maxHP>0){
            this.maxHP=maxHP;
        } else {
            this.maxHP=hp;
        }
    }
}