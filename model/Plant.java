public class Plant extends Entity {
    private int timer;
    private boolean inUse;

    Plant(Cell cell) {
        this.cell = cell;
        this.type = "Plant";
    }

    void turn() {
        if (inUse) {
            timer--;
            if (timer == 0) {
                inUse = false;
                destroy();
            }
        }
    }

    void startTimer() {
        if (inUse) return;
        inUse = true;
        timer = 5;
    }
}
