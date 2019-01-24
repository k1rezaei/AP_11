public class FarmAnimal extends Animal {
    static private int RATE_OF_HUNGER = 600;
    static private int MIN_HUNGER = 400;


    private String produceType;
    private int rateOfHunger;
    private int hungrySpeed;

    private int baseRemainTime;
    private int remainTime;

    FarmAnimal() {
    }

    FarmAnimal(String type) {
        super(type);
        rateOfHunger = RATE_OF_HUNGER;
        if (type.equalsIgnoreCase("cow")) {
            speed = 3;
            buyPrice = 1000;
            hungrySpeed = 36;
            remainTime = baseRemainTime = 450;
            produceType = "Milk";
        } else if (type.equalsIgnoreCase("chicken")) {
            speed = 5;
            buyPrice = 100;
            hungrySpeed = 6;
            remainTime = baseRemainTime = 300;
            produceType = "Egg";
        } else {
            speed = 4;
            buyPrice = 1000;
            hungrySpeed = 18;
            remainTime = baseRemainTime = 350;
            produceType = "Wool";
        }
    }

    FarmAnimal(String type, Cell cell) {
        this(type);
        this.cell = cell;
    }

    void move() {
        if (rateOfHunger < RATE_OF_HUNGER / 3) {
            setTargetCell(Game.getInstance().getMap().getClosest(Entity.PLANT, cell));
            super.move();
            super.move();
        } else {
            setTargetCell(Cell.getRandomCell());
            super.move();
        }

    }


    void turn() {
        rateOfHunger -= 3;
        if (rateOfHunger <= 0) {
            destroy();
            return;
        }
        move();
        if (remainTime == 0) {
            remainTime = baseRemainTime;
        }
        remainTime--;
    }


    void collide(Entity entity) {
        if (entity instanceof Plant) {
            if(rateOfHunger > MIN_HUNGER) return;
            ((Plant) (entity)).startTimer();
            rateOfHunger += RATE_OF_HUNGER / hungrySpeed;
            if (rateOfHunger > RATE_OF_HUNGER) rateOfHunger = RATE_OF_HUNGER;
        }
    }


    Item produce() {
        if (remainTime == 0) {
            Item item = new Item(produceType);
            Cell cell = new Cell(this.cell.getX(), this.cell.getY());
            item.setCell(cell);
            return item;
        }
        return null;
    }
}

