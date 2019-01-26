public abstract class Entity {
    final public static String WILD_ANIMAL = "WildAnimal";
    final public static String PLANT = "Plant";
    final public static String ITEM = "Item";
    protected Cell cell;
    protected Cell deadCell;
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
        if (type.equalsIgnoreCase("Sheep") || type.equalsIgnoreCase("Cow") || type.equalsIgnoreCase("Chicken"))
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

    public Cell getDeadCell() {
        return deadCell;
    }

    public String getType() {
        return type;
    }

    abstract void turn();

    void destroy() {
        deadCell = cell;


        cell = null;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

