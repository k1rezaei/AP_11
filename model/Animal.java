abstract public class Animal extends Entity {
    protected int speed;
    private Cell targetCell;
    private int state = 0;

    Animal() {
    }

    Animal(String type) {
        super(type);
    }

    Animal(String type, Cell cell) {
        super(type, cell);
    }


    void setTargetCell(Cell newTargetCell) {

        if (newTargetCell == null) newTargetCell = Cell.getRandomCell();

        if (targetCell == null || cell.equals(targetCell)) {
            targetCell = newTargetCell;
        }
    }

    void move() {
        if (targetCell == null) {
            throw new RuntimeException("targetCell is null");
        }
        for (int i = 0; i < speed; i++) state = cell.move(targetCell);
    }

    abstract void collide(Entity entity);

    abstract void turn();

    public int getState() {
        return state;
    }
}
