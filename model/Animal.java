abstract public class Animal extends Entity {
    static private int[] dx = {+1, 0, -1, 0};
    static private int[] dy = {0, +1, 0, -1};

    protected int speed;
    private Cell targetCell;

    Animal(String type) {
        super(type);
    }

    Animal(String type, Cell cell) {
        super(type, cell);
    }


    void setTargetCell(Cell newTargetCell) {
        if (newTargetCell == null) newTargetCell = Game.getInstance().getMap().getRandom();
        if (targetCell == null || cell.equals(targetCell)) {
            targetCell = newTargetCell;
        }
    }

    void move() {
        if (targetCell == null) {
            throw new RuntimeException("targetCell is null");
        }
        for (int i = 0; i < speed; i++) cell.move(targetCell);
    }

    abstract void collide(Entity entity);

    abstract void turn();
}
