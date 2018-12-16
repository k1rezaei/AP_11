import java.util.ArrayList;

public class Helicopter extends Vehicle {
    public Helicopter() {
        //TODO actual numbers
        setCapacity(5);
        setCurrentCapacity(5);
        setGoTime(7);
        setUpgradeCost(100);//TODO
    }

    public void go() {
        super.go();
        //TODO bebin hamin basse ya na
    }

    public ArrayList<Entity> getResult() {
        ArrayList<Entity> result = (ArrayList<Entity>) getItems().clone();
        clear();
        return result;
    }

    public void upgrade() {
        super.upgrade();
        //TODO bebin hamin bayad bashe ya na
    }

    public int getCost() {
        int priceSum = 0;
        for (Entity entity : getItems()) {
            priceSum += entity.buyPrice;
        }
        return priceSum;
    }
}
