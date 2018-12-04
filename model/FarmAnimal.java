public class FarmAnimal extends Animal{
    private String produceType;
    private int rateOfHunger;
    static private int RATE_OF_HUNGER = 100;
    FarmAnimal(String type){
        super(type);
        rateOfHunger = RATE_OF_HUNGER;
        if(type.equalsIgnoreCase("cow")){
            buyPrice = 1000;
            produceType = "Milk";
        }else if(type.equalsIgnoreCase("chicken")){
            buyPrice = 400;
            produceType = "Egg";
        }else{
            buyPrice = 1500;
            produceType = "Wool";
        }
    }
    FarmAnimal(String type, Cell cell){
        this(type);
        this.cell = cell;
    }

    void turn(){
        rateOfHunger --;
        if(rateOfHunger == 0){
            destroy();
            return;
        }
        move();
    }
    void collide(Entity entity){
        if(entity instanceof Plant){
            ((Plant)(entity)).startTimer();
            rateOfHunger = RATE_OF_HUNGER;
        }
    }
    Item produce(){
        return new Item(produceType);
    }
}