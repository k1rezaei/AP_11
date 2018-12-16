public class WildAnimal extends Animal {

    WildAnimal(String type) {
        super(type);
        if (type.equalsIgnoreCase("bear")) {
            sellPrice = 2000;
        } else if (type.equalsIgnoreCase("lion")) {
            sellPrice = 3000;
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
        return;
    }

    void move(){
        setTargetCell(Game.getInstance().getMap().getRandom());
        for(int i = 0; i < speed; i++) super.move();
    }

    @Override
    void turn() {
        move();
    }
}
