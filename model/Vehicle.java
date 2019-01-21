import java.util.ArrayList;

abstract public class Vehicle implements Upgradable {
    private int capacity;
    private int currentCapacity;
    private int upgradeCost;
    private int remainingTime = 0;
    private int goTime;
    private int level = 0;
    private int maxLevel = 3;
    private int capacityIncrease;
    private ArrayList<Entity> items = new ArrayList<>();

    public int getCapacityIncrease() {
        return capacityIncrease;
    }

    public void setCapacityIncrease(int capacityIncrease) {
        this.capacityIncrease = capacityIncrease;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getGoTime() {
        return goTime;
    }

    public void setGoTime(int goTime) {
        this.goTime = goTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getUpgradeCost() {
        return upgradeCost * level;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public ArrayList<Entity> getItems() {
        return items;
    }

    public void setItems(ArrayList<Entity> items) {
        this.items = items;
    }

    abstract public int getNeededMoney();

    abstract public ArrayList<Entity> getNeededItems();

    abstract public int getResultMoney();

    abstract public ArrayList<Entity> getResultItems();

    public void add(String type, int count) {
        if (remainingTime > 0) {
            throw new RuntimeException("Vehicle in use");
        }
        Entity entity = Entity.getNewEntity(type);
        if (currentCapacity >= entity.getSize() * count) {
            currentCapacity -= entity.getSize() * count;
            for (int i = 0; i < count; i++) {
                items.add(Entity.getNewEntity(type));
            }
        } else {
            throw new RuntimeException("Not enough space");
        }
    }

    public boolean turn() {
        if (remainingTime > 0) {
            remainingTime--;
            return remainingTime == 0;
        }
        return false;
    }

    public void go() {
        if (remainingTime > 0) {
            throw new RuntimeException("Vehicle in use");
        }
        remainingTime = goTime;
    }

    public void upgrade() {
        if (level < maxLevel) {
            capacity += capacityIncrease;
            currentCapacity += capacityIncrease;
            goTime--;
            level++;
        } else {
            throw new RuntimeException("Already at max level");
        }
    }

    public void clear() {
        setCurrentCapacity(capacity);
        items.clear();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Item:\n");
        for (Entity entity : items) {
            stringBuilder.append(entity.getType()).append("\n");
        }
        if (remainingTime > 0) {
            stringBuilder.append("Remaining time: ").append(remainingTime).append("\n");
        } else if (items.size() > 0) {
            stringBuilder.append("Ready to go!\n");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
