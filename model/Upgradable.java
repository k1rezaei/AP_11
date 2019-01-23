public interface Upgradable {

    RuntimeException MAX_LEVEL_EXCEPTION = new RuntimeException("MAX LEVEL EXCEPTION");

    String getName();

    int getUpgradeCost();

    void upgrade();

    int getLevel();

    int getMaxLevel();

    default boolean canUpgrade() {
        return getLevel() < getMaxLevel();
    }

    default boolean equals(Upgradable u) {
        return this.getName().equals(u.getName());
    }

}
