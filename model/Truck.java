public class Truck extends Vehicle {
    public void go() {
        super.go();
    }

    public int getResult() {
        int priceSum = 0;
        for (Entity entity : getItems()) {
            priceSum += entity.getSellPrice();
        }
        return priceSum;
    }
}
