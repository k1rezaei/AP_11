import java.util.HashMap;
import java.util.Map;

public class Workshop {
    private Map<String, Integer> inputs = new HashMap<>();
    private String output;
    private int duration, remainTime = -1, level = 1;
    private int x, y, upgradeCost;

    Workshop(HashMap<String, Integer> inputs, String output, int x, int y, int duration, int upgradeCost) {
        this.output = output;
        this.inputs = inputs;
        this.x = x; this.y = y;
        this.duration = duration;
        this.upgradeCost = upgradeCost;
    }

    void start() {
        if(remainTime != -1) throw new RuntimeException("Workshop is Working");
        Warehouse warehouse = Game.getInstance().getWarehouse();
        int buildNumber = Math.min(level, warehouse.getNumber(inputs));
        for (int i=0; i<buildNumber; i++) {
            warehouse.remove(inputs);
            remainTime = duration;
        }
        if(buildNumber == 0) throw new RuntimeException("Cant Build Anything");
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

    int getUpgradeCost() {
        return upgradeCost;
    }

    void upgrade() {
        level ++;
    }

}
