import java.util.ArrayList;

public class Game {
    private static Game game = new Game();
    private Map map;
    private int money;
    private Level level;
    private ArrayList<Workshop> workshops;
    private Helicopter helicopter;
    private Truck truck;
    private Well well;

    private Game() {
    }

    public static Game getInstance() {
        return game;
    }

    /*public static void main(String[] args) {

    }*/
    public void run(String command) {


    }

    public void buyAnimal(String name) {
        Entity animal = Entity.getNewEntity(name);
        if (animal.getBuyPrice() <= money) {
            money -= animal.getBuyPrice();
            map.addEntity(animal);
        } else {
            throw new RuntimeException("not enough money");
        }
    }

    public void addPlant(int x, int y) {
        if (well.getCurrentAmount() > 0) {
            Entity entity = new Plant(new Cell(x, y));
            well.decreaseWater();
            map.addEntity(entity);
        } else {
            throw new RuntimeException("not enough water");
        }
    }


    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public ArrayList<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(ArrayList<Workshop> workshops) {
        this.workshops = workshops;
    }

    public Helicopter getHelicopter() {
        return helicopter;
    }

    public void setHelicopter(Helicopter helicopter) {
        this.helicopter = helicopter;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Well getWell() {
        return well;
    }

    public void setWell(Well well) {
        this.well = well;
    }
}
