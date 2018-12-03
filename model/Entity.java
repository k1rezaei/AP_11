public abstract class Entity {
    protected Cell cell;
    protected int sellPrice, buyPrice;
    protected String type;

    public Cell getCell() {
        return cell;
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

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    Entity() {
    }

    Entity (String type) {
        this.type = type;
    }

    Entity (String type, Cell cell) {
        this.type = type;
        this.cell = cell;
    }

    abstract protected void turn();


    void destroy() {
        cell = null;
    }

    //TODO getNewEntity

}
