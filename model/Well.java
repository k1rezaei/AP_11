public class Well implements Upgradable {
    static private int fillCost = 100;
    int level;
    private int currentAmount;


    Well() {
        level = 1;
        currentAmount = 5;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public int getFillCost() {
        return fillCost;
    }

    public int getUpgradeCost() {
        if (level == 5) {
            throw new RuntimeException("Max level");
        }
        return 1000 * level;
    }

    public int getCapacity() {
        return 5 * level;
    }

    public boolean isItEmpty() {
        return currentAmount == 0;
    }

    public void decreaseWater() {
        if (currentAmount == 0) {
            throw new RuntimeException("Well is empty");
        }
        currentAmount--;
    }

    public void fill() {
        currentAmount = getCapacity();
    }

    public void upgrade() {
        if (level == 5) throw new RuntimeException("Max level");
        level++;
        fill();
    }
}
