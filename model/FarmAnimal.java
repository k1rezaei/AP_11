public class FarmAnimal extends Animal{
    String produceType;
    int rateOfHunger;
    FarmAnimal(String type){
        super(type);
        if(type.equalsIgnoreCase("cow")){
            buyPrice = 1000;
        }else if(type.equalsIgnoreCase("chicken")){
            buyPrice = 400;
        }else{
            buyPrice = 1500;
        }
    }
    FarmAnimal(String type, Cell cell){
        this(type);
        this.cell = cell;
    }
}
