public class FarmAnimal extends Animal{
    private String produceType;
    private int rateOfHunger;
    private int baseRemainTime;
    private int remainTime;
    static private int RATE_OF_HUNGER = 100;

    Cell targetCell;

    FarmAnimal(String type){
        super(type);
        rateOfHunger = RATE_OF_HUNGER;
        if (type.equalsIgnoreCase("cow")) {
            buyPrice = 10000;
            remainTime = baseRemainTime = 100;
            produceType = "Milk";
        } else if (type.equalsIgnoreCase("chicken")) {
            buyPrice = 400;
            remainTime = baseRemainTime = 20;
            produceType = "Egg";
        } else {
            buyPrice = 1500;
            remainTime = baseRemainTime = 50;
            produceType = "Wool";
        }
    }

    FarmAnimal(String type, Cell cell){
        this(type);
        this.cell = cell;
    }

    void move() {
        if(rateOfHunger > RATE_OF_HUNGER/2) super.move();
        else {
            if(targetCell == null)
                targetCell = Game.getInstance().getMap().getCloset(new Plant(-1,-1));
            cell.move(targetCell);
        }
    }

    //produce ro ghabl az turn bayad seda koni.

    void turn() {
        rateOfHunger--;
        if (rateOfHunger == 0) {
            destroy();
        }
        move();
        if (remainTime == 0) {
            remainTime = baseRemainTime;
        }
        remainTime--;
    }


    void collide(Entity entity){
        if(entity instanceof Plant){
            ((Plant)(entity)).startTimer();
            rateOfHunger = RATE_OF_HUNGER;

        }
    }


    Item produce() {
        if(remainTime == 0) return new Item(produceType);
        return null;
    }
}

