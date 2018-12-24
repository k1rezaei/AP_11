public interface Upgradable {

    String getName();
    int getUpgradeCost();

    void upgrade();
    int getLevel();
    int getMaxLevel();

    default boolean canUpgrade() {
        return getLevel() < getMaxLevel();
    }

}
