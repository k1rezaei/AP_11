public interface Upgradable {

    String getName();

    int getUpgradeCost();

    void upgrade();

    int getMaxLevel();
    int getLevel();

    static public RuntimeException MAX_LEVEL_EXCEPTION = new RuntimeException("Max LeveL");
}
