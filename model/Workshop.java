import java.util.HashMap;
import java.util.Map;

public class Workshop implements Upgradable {
    public static final int MAX_LEVEL = 5;
    private Map<String, Integer> inputs = new HashMap<>();
    private String output, name;
    private int duration, remainTime = -1, level = 1;
    private int x, y, upgradeCost, startCost;

    Workshop(HashMap<String, Integer> inputs, String output, int x, int y, int duration, int upgradeCost, int startCost, String name) {
        this.output = output;
        this.inputs = inputs;
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.upgradeCost = upgradeCost;
        this.startCost = startCost;
        this.name = name;
    }

    public int getStartCost() {
        return startCost;
    }

    public String getName() {
        return name;
    }

    void start() {
        if (remainTime != -1) throw new RuntimeException("Workshop is Working");
        Warehouse warehouse = Game.getInstance().getWarehouse();
        int buildNumber = Math.min(level, warehouse.getNumber(inputs));
        for (int i = 0; i < buildNumber; i++) {
            warehouse.remove(inputs);
        }
        if (buildNumber == 0) throw new RuntimeException("Cant Build Anything");
        remainTime = duration;
    }

    void turn() {
        if (remainTime == -1) return;
        if (remainTime == 0) {
            Entity entity = new Item(output, new Cell(x, y));
            Game.getInstance().addEntity(entity);
            remainTime = -1;
            return;
        } else remainTime--;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void upgrade() {
        level++;
        //TODO duration
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(name).append(" is level : ").append(getLevel()).append('\n');
        if(remainTime == -1) result.append(name + " is free now.\n");
        else {
            result.append(name).append(" is working now.\n");
            result.append(name + "will finish it's work in next " + remainTime + "\n");
        }

        result.append(name).append(" Makes ").append(output).append(" from these Items : \n");
        for (Map.Entry<String, Integer> entry : inputs.entrySet()) {
            result.append(entry.getValue()).append(" *  ").append(entry.getKey()).append("\n");
        }

        return result.toString();
    }
}
