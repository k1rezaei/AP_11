import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Warehouse implements Upgradable {
    public static final int INF = 1000 * 1000 * 10;
    Map<String, Integer> storables = new HashMap<>();
    private int capacity = 10000;
    private int upgradeCost = 1000;
    private int CAPACITY_RATE_CHANGE = 500;
    private String name = "warehouse";

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void upgrade() {
        capacity += CAPACITY_RATE_CHANGE;
    }

    void add(String type) {
        int count = storables.get(type);
        count++;
        storables.put(type, count);
    }

    void add(Entity entity) {
        if(entity.getSize() > getCapacity()) throw new RuntimeException("WareHouse does not have enough capacity.");
        capacity -= entity.getSize();
        String type = entity.getType();
        add(type);
    }

    Entity remove(String type) {
        if(storables.get(type) == null) throw new RuntimeException("Item is Not in WareHouse.");
        int count = storables.get(type);
        count--;
        Entity entity = Entity.getNewEntity(type);
        capacity += entity.getSize();
        storables.put(type, count);
        return entity;
    }

    void remove(Map<String, Integer> test) {
        for (String type : test.keySet()) {
            if(storables.get(type) == null)  throw new RuntimeException("Item is Not in WareHouse");
            int count = storables.get(type);
            count -= test.get(type);
            Entity entity = Entity.getNewEntity(type);
            capacity += test.get(type) * entity.getSize();
            storables.put(type, count);
        }
    }

    int getNumber(Map<String, Integer> test) {
        int ans = INF;
        for (String str : test.keySet()) {

            int cnt = 0;
            if(storables.get(str) != null) cnt = storables.get(str);

            ans = Math.min(ans, cnt / test.get(str));
        }
        return ans;
    }

    int getNumber(ArrayList<Entity> entities) {
        Map<String, Integer> map = new HashMap<>();
        for (Entity entity : entities) {
            String type = entity.getType();

            int cnt = 0;
            if(map.get(type) != null) cnt = map.get(type);
            cnt ++;

            map.put(type, cnt);
        }
        return getNumber(map);
    }


}
