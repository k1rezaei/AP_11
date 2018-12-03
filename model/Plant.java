public class Plant extends Entity {
    private int timer;
    private boolean inUse;

    Plant(Cell cell){
        this.cell = cell;
        this.type = "Plant";
    }
g
    boolean turn(){
        if(inUse){
            timer --;
            if(timer == 0){
                inUse = false;
                return true;
            }
        }
        return false;
    }

    void startTimer(){
        timer = 5;
    }
}
