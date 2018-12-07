import java.util.ArrayList;

abstract public class Vehicle implements Upgradable {
    private int capacity;
    private int currentCapacity;
    private int upgradeCost;
    private int remainingTime;
    private int goTime;
    private ArrayList<Entity> items = new ArrayList<>();

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
        return upgradeCost;
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

    public void go(){
        if(remainingTime>0){
            throw new RuntimeException("Vehicle in use");
        }
        remainingTime=goTime;
    }

    public void upgrade() {
        capacity++;
        currentCapacity++;
        goTime--;
        //TODO actual upgrade numbers
    }
    public void clear(){
        items.clear();;
    }
}