public class WildAnimal extends Animal {

    WildAnimal() {
    }

    WildAnimal(String type) {
        super(type);
        if (type.equalsIgnoreCase("bear")) {
            sellPrice = 2000;
            speed = 4;
            size = 10;
        } else if (type.equalsIgnoreCase("lion")) {
            sellPrice = 3000;
            speed = 5;
            size = 7;
        }
    }

    WildAnimal(String type, Cell cell) {
        this(type);
        this.cell = cell;
    }

    void collide(Entity entity) {
        if (entity instanceof FarmAnimal) {
            entity.destroy();
        }
      /*  if (entity instanceof Cat)
            entity.destroy();*/
        return;
    }

    void move() {
        setTargetCell(Cell.getRandomCell());
        super.move();
    }

    @Override
    void turn() {
        move();
    }
}
