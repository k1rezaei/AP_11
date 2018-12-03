import java.util.ArrayList;

abstract public class Vehicle {
    private int capacity, upgradeCost, remainingTime;
    private ArrayList<Entity> items=new ArrayList<>();

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

    public void add(String type,int count){

    }
    private void add(String type) throws Exception{

    }

}
