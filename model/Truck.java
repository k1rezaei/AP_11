public class Truck extends Vehicle {
    public void go() {
        super.go();
        //TODO bebin hamin basse ya na
    }
    public Truck(){
        //TODO actual numbers
        setCapacity(5);
        setCurrentCapacity(5);
        setGoTime(7);
        setUpgradeCost(100);//TODO
    }
    public int getResult() {
        int priceSum = 0;
        for (Entity entity : getItems()) {
            priceSum += entity.getSellPrice();
        }
        clear();
        return priceSum;
    }

    public void upgrade() {
        super.upgrade();
        //TODO bebin hamin bayad bashe ya na
    }
}
