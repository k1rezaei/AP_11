public class Dog extends Animal{

    Cell targetCell;

    Dog(){
        super("Dog");
    }
    Dog(Cell cell){
        super("Dog", cell);
    }

    /// bad az collid momkene Dogemoo Destroy she!!!
    void collide(Entity entity){
        if(entity instanceof WildAnimal){
            entity.destroy();
            destroy();
        }
    }
    void move(){
        setTargetCell(Game.getInstance().getMap().getCloset("wildanimal"));
        for(int i = 0; i < speed; i++) super.move();
    }
    void turn(){
        move();
    }
}

