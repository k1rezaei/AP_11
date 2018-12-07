public class Dog extends Animal {
    Dog() {
        super("Dog");
    }

    Dog(Cell cell) {
        super("Dog", cell);
    }

    void move() {
        Map map = Game.getInstance().getMap();
        Cell targetCell = map.getClosetWildAnimal();
        if (targetCell == null) {
            super.move();
        } else {
            cell.moveCloser(targetCell);
        }
    }

    /// bad az collid momkene Dogemoo Destroy she!!!
    void collide(Entity entity) {
        if (entity instanceof WildAnimal) {
            entity.destroy();
            destroy();
        }
    }

    void turn() {
        move();
    }
}
