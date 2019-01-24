import java.util.HashMap;
import java.util.Map;

public class Workshop implements Upgradable {
    public static final int MAX_LEVEL = 4;
    private Map<String, Integer> inputs;
    private String output, name;
    private int duration, remainTime = -1, level = 0;
    private int x, y, upgradeCost, startCost;
    private int numberOfOutputs;

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

    Workshop(Workshop workshop){
        output = workshop.output;
        inputs = workshop.inputs;
        x = workshop.x;
        y = workshop.y;
        duration = workshop.duration;
        upgradeCost = workshop.upgradeCost;
        startCost = workshop.startCost;
        name = workshop.name;
    }

    public int getStartCost() {
        return startCost;
    }

    public void setStartCost(int startCost) {
        this.startCost = startCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void start() {
        if (remainTime != -1) throw new RuntimeException("Workshop is Working");
        Warehouse warehouse = Game.getInstance().getWarehouse();
        int buildNumber = Math.min(level + 1, warehouse.getNumber(inputs));
        for (int i = 0; i < buildNumber; i++) {
            warehouse.remove(inputs);
        }
        if (buildNumber == 0) throw new RuntimeException("Cant Build Anything");
        remainTime = duration;
        numberOfOutputs = buildNumber;
    }

    void turn() {
        if (remainTime == -1) return;
        int z = 0;
        if(x > 200) z = -1;
        else z = 1;

        if (remainTime == 0) {
            for (int i = 0; i < numberOfOutputs; i++) {
                Entity entity = new Item(output, new Cell(z * 25 + x + 20 * i * z, y));
                Game.getInstance().addEntity(entity);
            }
            remainTime = -1;
            return;
        } else remainTime--;
    }

    public int getUpgradeCost() {
        return (level + 1) * upgradeCost;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
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

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(name).append(" is level : ").append(getLevel()).append('\n');
        if (remainTime == -1) result.append(name + " is free now.\n");
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

    public Map<String, Integer> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Integer> inputs) {
        this.inputs = inputs;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getNumberOfOutputs() {
        return numberOfOutputs;
    }

    public void setNumberOfOutputs(int numberOfOutputs) {
        this.numberOfOutputs = numberOfOutputs;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
