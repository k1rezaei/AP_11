public class Well implements Upgradable{
    private int currentAmount;
    int level;
    static private int fillCost = 100;


    Well(){
        level = 1;
        currentAmount = 5;
    }

    public int getCurrentAmount(){ return currentAmount; }
    public int getFillCost(){ return fillCost; }
    public int getUpgradeCost(){ return 1000*level; }
    public int getCapacity(){ return 5*level; }

    public boolean isItEmpty(){
        return currentAmount == 0;
    }
    public void decreaseWater(){
        if(currentAmount == 0){
            throw new RuntimeException("WellIsEmpty");
        }
        currentAmount --;
    }

    public void fill(){
        currentAmount = getCapacity();
    }
    public void upgrade(){
        level ++;
        fill();
    }
}
