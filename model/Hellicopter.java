import java.util.ArrayList;

public class Hellicopter extends Vehicle {
    public void go() {
        super.go();
        //TODO bebin hamin basse ya na
    }

    public ArrayList<Entity> getResult() {
        ArrayList<Entity> result = (ArrayList<Entity>) getItems().clone();
        clear();
        return result;
    }

    public void upgrade() {
        super.upgrade();
        //TODO bebin hamin bayad bashe ya na
    }

    public int getCost() {
        int priceSum = 0;
        for (Entity entity : getItems()) {
            priceSum += entity.buyPrice;
        }
        return priceSum;
    }
}
