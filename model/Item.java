public class Item extends Entity {

    private int size;

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
                break ;
            case "DriedEggs" :
                fix(4, 100, 50);
                break;
            case "Cake" :
                fix(5, 200, 100);



        }
    }

    private void fix(int size, int buyCost, int sellCost) {
        this.size = size;
        this.sellPrice = sellCost;
        this.buyPrice = buyCost;
    }

    Item(String type, Cell cell) {
        this(type);
        this.cell = cell;
    }
}
