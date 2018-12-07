import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    Map<String, Integer> storables = new HashMap<>();
    private int capacity = 10000;
    private int upgradeCost = 1000;
    private int CAPACITY_RATE_CHANGE = 500;

    public int getCapacity() {
        return capacity;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    void upgrade() {
        capacity += CAPACITY_RATE_CHANGE;
    }

    void add(String type) {
        int count = storables.get(type);
        count ++;
        storables.put(type, count);
    }

    void add(Entity entity) {
        String type = entity.getType();
        add(type);
    }

    Entity remove(String type) {
        int count = storables.get(type);
        count --;
        storables.put(type, count);
        return Entity.getNewEntity(type);
    }

}
