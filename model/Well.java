public class Well implements Upgradable {
    static private int[] fillCost = {19, 17, 15, 7};
    static private int[] capacity = {5, 7, 10, 100};
    static private int[] upgradeCost = {30, 35, 100};
    private int level;
    private int currentAmount;

    Well() {
        level = 0;
        currentAmount = 5;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public int getFillCost() {
        return fillCost[level];
    }

    public int getUpgradeCost() {
        if (level == 3) {
            throw Upgradable.MAX_LEVEL_EXCEPTION;
        }
        return upgradeCost[level];
    }

    public int getCapacity() {
        return capacity[level];
    }

    public boolean isEmpty() {
        return currentAmount == 0;
    }

    public void decreaseWater() {
        if (currentAmount == 0) {
            throw new RuntimeException("Well is empty");
        }
        currentAmount--;
    }

    public void fill() {
        if (currentAmount != 0) throw new RuntimeException("Well is not empty");
        currentAmount = getCapacity();
    }

    public void upgrade() {
        if (level == 3) throw Upgradable.MAX_LEVEL_EXCEPTION;
        level++;
        currentAmount = getCapacity();
    }

    public String getName() {
        return "well";
    }

    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String toString() {
        return "ٌٍLevel : " + level + " | currentAmount : " + currentAmount + "\n";
    }
}