public class Cat extends Animal {
    static private int level = 0;

    Cat() {
        super("cat");
        buyPrice = 2500;
        speed = 4;
    }


    Cat(Cell cell) {
        super("cat", cell);
        buyPrice = 2500;
        speed = 4;
    }

    static public void upgrade() {
        if (level == 1) throw Upgradable.MAX_LEVEL_EXCEPTION;
        level = 1;
    }

    static public int getUpgradeCost() {
        return 500;
    }

    static public int getMaxLevel() {
        return 1;
    }

    static public int getLevel() {
        return level;
    }

    static public void setLevel(int level) {
        Cat.level = level;
    }

    public String getName() {
        return "Cat";
    }

    void collide(Entity entity) {
        if (entity instanceof Item) {
            Game.getInstance().pickUp(entity.getCell().getX(), entity.getCell().getY());
        }
    }

    void move() {
        if (level == 0) {
            setTargetCell(Game.getInstance().getMap().getClosest(Entity.ITEM, Cell.getRandomCell()));
        } else {
            setTargetCell(Game.getInstance().getMap().getClosest(Entity.ITEM, cell));
        }
        super.move();
    }

    void turn() {
        move();
    }
}
