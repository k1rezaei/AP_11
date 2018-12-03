public class Entity {
    private Cell cell;
    protected int sellPrice, buyPrice;
    private String type;

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

    Entity (String tyoe) {
        this.type = type;
    }

    Entity (String name, Cell cell) {
        this.type = type;
        this.cell = cell;
    }



    //TODO getNewEntity


}
