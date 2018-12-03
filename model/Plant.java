public class Plant extends Entity {
    private int timer;
    private boolean inUse;

    Plant(Cell cell){
        if(cell == null){
            throw new RuntimeException("cell is NULL");
        }
        this.cell = cell;
        this.type = "Plant";
    }

    void turn(){
        if(inUse){
            timer --;
            if(timer == 0){
                inUse = false;
                destory();
            }
        }
    }

    void startTimer(){
        if(inUse) return;
        timer = 5;
    }
}
