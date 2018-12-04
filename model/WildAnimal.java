
public class WildAnimal extends Animal {

    private int size = 0;

    int getSize() {
        return size;
    }

    void collide(Entity entity) {
        if(entity instanceof FarmAnimal) {
            entity.destroy();
        }
        return ;
    }

    WildAnimal(String type) {
        super(type);
        if(type.equalsIgnoreCase("bear")) {
            sellPrice = 2000;
        }else if(type.equalsIgnoreCase("lion")){
            sellPrice = 3000;
        }
    }

    WildAnimal(String type, Cell cell) {
        this(type);
        this.cell = cell;
    }

    @Override
    void turn() {
        move();
    }
}
