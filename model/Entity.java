import java.util.Objects;

public abstract class Entity {
    final public static String WILD_ANIMAL = "WildAnimal";
    final public static String PLANT = "Plant";
    final public static String ITEM = "Item";
    protected Cell cell;
    protected int sellPrice, buyPrice, size;
    protected String type;

    public Entity() {
    }

    Entity(String type) {
        this.type = type;
    }

    Entity(String type, Cell cell) {
        this.type = type;
        this.cell = cell;
    }

    static Entity getNewEntity(String type) {
        if (type.equalsIgnoreCase("Sheep") || type.equalsIgnoreCase("Cow") || type.equals("Chicken"))
            return new FarmAnimal(type);
        else if (type.equalsIgnoreCase("Lion") || type.equalsIgnoreCase("Bear"))
            return new WildAnimal(type);
        else if (type.equalsIgnoreCase("Cat"))
            return new Cat();
        else if (type.equalsIgnoreCase("Dog"))
            return new Dog();
        return new Item(type);
    }

    public int getState() {
        return 0;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public String getType() {
        return type;
    }

    abstract void turn();

    void destroy() {
        cell = null;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return sellPrice == entity.sellPrice &&
                buyPrice == entity.buyPrice &&
                size == entity.size &&
                Objects.equals(cell, entity.cell) &&
                Objects.equals(type, entity.type);
    }

    @Override
    public int hashCode() {
        return 0;//Objects.hash(cell, sellPrice, buyPrice, size, type);
    }
}

