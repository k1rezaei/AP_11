public abstract class Entity {
    final public static String WILD_ANIMAL = "WildAnimal";
    final public static String PLANT = "Plant";
    final public static String ITEM = "Item";
    protected Cell cell;
    protected int sellPrice, buyPrice, size;
    protected String type;

    public Entity() {}

    Entity(String type) {
        this.type = type;
    }

    Entity(String type, Cell cell) {
        this.type = type;
        this.cell = cell;
    }

    static Entity getNewEntity(String type) {
        if (type.equals("Sheep") || type.equals("Cow") || type.equals("Chicken"))
            return new FarmAnimal(type);
        else if (type.equals("Lion") || type.equals("Bear"))
            return new WildAnimal(type);
        return new Item(type);
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

    //TODO getNewEntity

    void destroy() {
        cell = null;
    }

    public int getSize() {
        return size;
    }
}

