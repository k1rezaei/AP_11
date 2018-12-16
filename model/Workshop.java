import java.util.HashMap;
import java.util.Map;

public class Workshop implements Upgradable{
    private Map<String, Integer> inputs = new HashMap<>();
    private String output, name;
    private int duration, remainTime = -1, level = 1;
    private int x, y, upgradeCost;

    Workshop(HashMap<String, Integer> inputs, String output, int x, int y, int duration, int upgradeCost, String name) {
        this.output = output;
        this.inputs = inputs;
        this.x = x; this.y = y;
        this.duration = duration;
        this.upgradeCost = upgradeCost;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void start() {
        if(remainTime != -1) throw new RuntimeException("Workshop is Working");
        Warehouse warehouse = Game.getInstance().getWarehouse();
        int buildNumber = Math.min(level, warehouse.getNumber(inputs));
        for (int i=0; i<buildNumber; i++) {
            warehouse.remove(inputs);
        }
        if(buildNumber == 0) throw new RuntimeException("Cant Build Anything");
        remainTime = duration;
    }

    void turn() {
        if(remainTime == 0) {
            Entity entity = new Item(output, new Cell(x, y));
            Game.getInstance().addEntity(entity);
            remainTime = -1;
            return ;
        }
        else remainTime --;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void upgrade() {
        level ++;
    }

}
