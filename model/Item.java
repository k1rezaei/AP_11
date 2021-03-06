public class Item extends Entity {
    private int remainTime = 400;

    Item() {
    }

    Item(String type) {
        super(type);
        switch (type) {
            case "Egg":
                fix(1, 20, 10);
                break;
            case "Wool":
                fix(5, 200, 100);
                break;
            case "Plume":
                fix(2, 200, 100);
                break;
            case "Milk":
                fix(10, 2000, 1000);
                break;
            case "Horn":
                fix(6, 2000, 1000);
                break;
            case "DriedEggs":
                fix(4, 100, 50);
                break;
            case "EggPowder":
                fix(4, 100, 50);
                break;
            case "Cookie":
                fix(5, 200, 100);
                break;
            case "Cake":
                fix(6, 400, 200);
                break;
            case "Sewing":
                fix(3, 300, 150);
                break;
            case "Fabric":
                fix(6, 400, 300);
                break;
            case "CarnivalDress":
                fix(8, 1400, 1300);
                break;
            case "SourCream":
                fix(8, 3000, 1500);
                break;
            case "Curd":
                fix(6, 4000, 2000);
                break;
            case "Cheese":
                fix(5, 5000, 2500);
                break;
            case "ColoredPlume":
                fix(2, 300, 150);
                break;
            case "Adornment":
                fix(4, 400, 300);
                break;
            case "BrightHorn":
                fix(5, 3000, 1500);
                break;
            case "Intermediate":
                fix(4, 4000, 2000);
                break;
            case "Souvenir":
                fix(3, 5000, 2500);
                break;
            case "Flour":
                fix(1, 20, 10);
                break;
            case "CheeseFerment":
                fix(2, 25, 15);
                break;
            case "Varnish":
                fix(2, 25, 15);
                break;
            case "MegaPie":
                fix(20, 20000, 10000);
                break;
            case "SpruceGrizzly":
                fix(25, 2500, 7000);
                break;
            case "SpruceLion":
                fix(25, 2500, 7000);
                break;
            case "SpruceBrownBear":
                fix(25, 2500, 7000);
                break;
            case "SpruceJaguar":
                fix(25, 2500, 7000);
                break;
            case "SpruceWhiteBear":
                fix(25, 2500, 7000);
                break;
            case "CagedGrizzly":
                fix(20, 80, 80);
                break;
            case "CagedLion":
                fix(20, 150, 150);
                break;
            case "CagedBrownBear":
                fix(20, 150, 150);
                break;
            case "CagedJaguar":
                fix(20, 200, 200);
                break;
            case "CagedWhiteBear":
                fix(20, 100, 100);
                break;
            default:
                throw new RuntimeException("Item Not Found");
        }
    }

    Item(String type, Cell cell) {
        this(type);
        this.cell = cell;
    }

    private void fix(int size, int buyCost, int sellCost) {
        this.size = size;
        this.sellPrice = sellCost;
        this.buyPrice = buyCost;
    }

    @Override
    void turn() {
        remainTime--;
        if (remainTime <= 0) destroy();
    }

    public int getRemainTime() {
        return remainTime;
    }
}
