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
    private Warehouse warehouse;

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
            well.decreaseWater();
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    Entity entity = new Plant(new Cell(x + i, y + j));
                    map.addEntity(entity);
                }
            }
        } else {
            throw new RuntimeException("not enough water");
        }
    }

    public void cage(int x, int y) {
        WildAnimal wildAnimal = map.cage(new Cell(x, y));
        if (wildAnimal != null) {
            warehouse.add(wildAnimal);
        }
    }

    public void pickUp(int x, int y) {
        Item item = map.pickUp(new Cell(x, y));
        if (item != null) {
            warehouse.add(item);
        }
    }

    public void well() {
        if (well.getFillCost() <= money) {
            money -= well.getFillCost();
            well.fill();
        }
    }

    public void upgrade(String type) {
        switch (type) {
            case "cat":
            case "dog":
                //TODO what the **** should we do about these?
                break;
            case "warehouse":
                if(warehouse.getUpgradeCost()<=money){//TODO check warehouse isnt at max level
                    money-=warehouse.getUpgradeCost();
                    warehouse.upgrade();
                }
                break;
            case "truck":
                if(truck.getUpgradeCost()<=money){
                    money-=truck.getUpgradeCost();
                }
        }
    }

    public void addEntity(Entity entity) {
        map.addEntity(entity);
    }

    public boolean checkLevel() {
        return money >= level.getGoalMoney() && warehouse.getNumber(level.getGoalEntity()) > 0;
    }

    public void turn(int n) {
        for (int i = 0; i < n; i++) {
            turn();
        }
    }

    public void turn() {
        map.turn();
        truck.turn();
        helicopter.turn();
        for (Workshop workshop : workshops) {
            workshop.turn()
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
