public class Dog extends Animal{

    Cell targetCell;

    Dog(){
        super("Dog");
    }
    Dog(Cell cell){
        super("Dog", cell);
    }
    void move(){
        cell.move(targetCell);
    }

    /// bad az collid momkene Dogemoo Destroy she!!!
    void collide(Entity entity){
        if(entity instanceof WildAnimal){
            entity.destroy();
            destroy();
        }
    }
    void turn(){
        if(targetCell == null) {
            Map map = Game.getInstance().getMap();targetCell = map.getClosetWildAnimal();
        }
        move();
    }
}
>>>>>>> origin/keivan
