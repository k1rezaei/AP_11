public interface Upgradable {

    String getName();
    int getUpgradeCost();

    void upgrade();
    int getLevel();
    int getMaxLevel();

    RuntimeException MAX_LEVEL_EXCEPTION = new RuntimeException("MAX LEVEL EXCEPTION");

    default boolean canUpgrade() {
        return getLevel() < getMaxLevel();
    }

}
