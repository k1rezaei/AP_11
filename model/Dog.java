public class Dog extends Animal {

    Dog() {
        super("dog");
        speed = 6;
        buyPrice = 1000;
    }


    Dog(Cell cell) {
        super("dog", cell);
        speed = 6;
        buyPrice = 1000;
    }


    /// bad az collid momkene Dogemoo Destroy she!!!
    void collide(Entity entity) {
        if (entity instanceof WildAnimal) {
            entity.destroy();
            destroy();
        }
    }

    void move() {
        setTargetCell(Game.getInstance().getMap().getClosest(Entity.WILD_ANIMAL, cell));
        super.move();
    }

    void turn() {
        move();
    }
}

