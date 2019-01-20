import java.util.ArrayList;

public class Truck extends Vehicle implements Upgradable {
    private static final int INITIAL_CAPACITY = 1000;
    private static final int CAPACITY_INCREASE = 200;
    private static final int UPGRADE_COST = 300;
    private static final int GO_TIME = 200;

    public Truck() {
        setCapacity(INITIAL_CAPACITY);
        setCurrentCapacity(getCapacity());
        setCapacityIncrease(CAPACITY_INCREASE);
        setGoTime(GO_TIME);
        setUpgradeCost(UPGRADE_COST);
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

    public String getName() {
        return "truck";
    }
}
