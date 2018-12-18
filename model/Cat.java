public class Cat extends Animal implements Upgradable {
    private int level;

    Cat() {
        super("Cat");
        buyPrice = 2500;
        speed = 4;
    }

    Cat(Cell cell) {
        super("Cat", cell);
        buyPrice = 2500;
        speed = 4;
    }

    public void upgrade() {
        if (level == 1) throw new RuntimeException("Max Level");
        level = 1;
    }

    public int getUpgradeCost() {
        if (level == 1) throw new RuntimeException("Max Level");
        return 500;
    }

    public String getName() {
        return "Cat";
    }

    void collide(Entity entity) {
        if (entity instanceof Item) {
            entity.destroy();
            /// inja bayad chi kar konam??:(
        }
    }

    void move() {
        if (level == 0) {
            setTargetCell(Game.getInstance().getMap().getRandom());
        } else {
            setTargetCell(Game.getInstance().getMap().getCloset(Entity.ITEM, cell));
        }
        super.move();
    }

    void turn() {
        move();
    }
}
