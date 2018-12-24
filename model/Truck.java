import java.util.ArrayList;

public class Truck extends Vehicle implements Upgradable {
    public Truck() {
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

    public int getResultMoney() {
        int priceSum = 0;
        for (Entity entity : getItems()) {
            priceSum += entity.getSellPrice();
        }
        clear();
        return priceSum;
    }

    @Override
    public ArrayList<Entity> getResultItems() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Entity> getNeededItems() {
        return getItems();
    }

    @Override
    public int getNeededMoney() {
        return 0;
    }

    public int getUpgradeCost() {
        return super.getUpgradeCost();
    }

    public void upgrade() {
        super.upgrade();
        //TODO bebin hamin bayad bashe ya na
    }

    public String getName() {
        return "truck";
    }
}
