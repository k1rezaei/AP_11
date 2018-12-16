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
        count++;
        storables.put(type, count);
    }

    void add(Entity entity) {
        String type = entity.getType();
        add(type);
    }

    Entity remove(String type) {
        int count = storables.get(type);
        count--;
        storables.put(type, count);
        return Entity.getNewEntity(type);
    }

    int getNumber(Map<String, Integer> test) {
        int ans = 1000 * 1000;
        for (String str : test.keySet()) {
            ans = Math.min(ans, storables.get(str) / test.get(str));
        }
        return ans;
    }

    void remove(Map<String, Integer> test) {
        for (String str : test.keySet()) {
            int curr = storables.get(str);
            curr -= test.get(str);
            storables.put(str, curr);
        }
    }

}
