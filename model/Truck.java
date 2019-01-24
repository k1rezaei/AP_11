import java.util.ArrayList;

public class Truck extends Vehicle implements Upgradable {
    private static final int INITIAL_CAPACITY = 20;
    private static final int CAPACITY_INCREASE = 8;
    private static final int UPGRADE_COST = 300;
    private static final int GO_TIME = 200;

    public Truck() {
        setCapacity(INITIAL_CAPACITY);
        setCurrentCapacity(getCapacity());
        setCapacityIncrease(CAPACITY_INCREASE);
        setGoTime(GO_TIME);
        setUpgradeCost(UPGRADE_COST);
    }

    public int getResultMoneyWithoutClear(){
        int priceSum = 0;
        for (String type: getItems()) {
            Entity entity = Entity.getNewEntity(type);
            priceSum += entity.getSellPrice();
        }
        return priceSum;
    }

    public int getResultMoney() {
        int priceSum = getResultMoneyWithoutClear();
        clear();
        return priceSum;
    }

    @Override
    public ArrayList<Entity> getResultItems() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Entity> getNeededItems() {
        ArrayList<Entity> neededItems = new ArrayList<>();
        for(String type: getItems()){
            neededItems.add(Entity.getNewEntity(type));
        }
        return  neededItems;
    }

    @Override
    public int getNeededMoney() {
        return 0;
    }

    public String getName() {
        return "truck";
    }
}
