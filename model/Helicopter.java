import java.util.ArrayList;

public class Helicopter extends Vehicle implements Upgradable {
    private static final int INITIAL_CAPACITY = 20;
    private static final int CAPACITY_INCREASE = 10;
    private static final int GO_TIME = 200;
    private static final int UPGRADE_COST = 200;

    public Helicopter() {
        setCapacity(INITIAL_CAPACITY);
        setCurrentCapacity(getCapacity());
        setCapacityIncrease(CAPACITY_INCREASE);
        setGoTime(GO_TIME);
        setUpgradeCost(UPGRADE_COST);
    }

    public ArrayList<Entity> getResultItems() {
        ArrayList<Entity> result = new ArrayList<>();
        for (String type : getItems()) {
            result.add(Entity.getNewEntity(type));
        }
        clear();
        return result;
    }

    public int getResultMoney() {
        return 0;
    }

    @Override
    public ArrayList<Entity> getNeededItems() {
        return new ArrayList<>();
    }

    @Override
    public int getNeededMoney() {
        int priceSum = 0;
        for (String type : getItems()) {
            Entity entity = Entity.getNewEntity(type);
            priceSum += entity.buyPrice;
        }
        return priceSum;
    }

    public String getName() {
        return "helicopter";
    }
}
