import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private static Game game = new Game();
    private Map map = new Map();
    private int money;
    private Level level;
    private ArrayList<Workshop> workshops = new ArrayList<>();
    private Helicopter helicopter = new Helicopter();
    private Truck truck = new Truck();
    private Well well = new Well();
    private Warehouse warehouse = new Warehouse();
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ArrayList<Upgradable> upgradables = new ArrayList<>();//TODO add upgradables
    private int currentTurn;

    private Game() {
        vehicles.add(truck);
        vehicles.add(helicopter);
        upgradables.add(warehouse);
        upgradables.addAll(workshops);
        upgradables.add(well);
        upgradables.addAll(vehicles);
    }

    public static Game getInstance() {
        return game;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (true) {
            String command = input.nextLine();
            game.run(command);
        }
    }

    public void run(String command) {
        String[] commands = command.split("(\\s)*");
        Vehicle vehicle = null;
        try {
            switch (commands[0]) {
                case "run":
                    //TODO
                    break;
                case "save":
                    //TODO
                    break;
                case "load":
                    //TODO
                    break;
                case "print":
                    //TODO
                    break;
                case "turn":
                    turn(Integer.parseInt(commands[1]));
                    break;
                case "buy":
                    buyAnimal(commands[1]);
                    break;
                case "pickup":
                    pickUp(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
                    break;
                case "cage":
                    cage(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
                    break;
                case "well":
                    well();
                    break;
                case "start":
                    for (Workshop workshop : workshops) {
                        if (workshop.getName().equals(commands[1])) {
                            workshop.start();
                        }
                    }
                    break;
                case "upgrade":
                    upgrade(commands[1]);
                    break;
                case "truck":
                    vehicle = truck;
                case "helicopter":
                    if (vehicle == null) {
                        vehicle = helicopter;
                    }
                    switch (commands[1]) {
                        case "go":
                            if (money >= vehicle.getNeededMoney() && warehouse.getNumber(vehicle.getNeededItems()) > 0) {//TODO warehouse.number ba arraylist entity
                                money -= vehicle.getNeededMoney();
                                for (Entity entity : vehicle.getNeededItems()) {
                                    warehouse.remove(entity.type);
                                }
                                vehicle.go();
                            } else {
                                throw new RuntimeException("vehicle requirements not met");
                            }
                            //TODO change inside vehicle
                            break;
                        case "clear":
                            vehicle.clear();
                            break;
                        case "add":
                            vehicle.add(commands[2], Integer.parseInt(commands[3]));
                            break;
                    }
                    break;
            }

        } catch (Exception e) {
            //TODO view
            System.out.println(e.getMessage());
        }
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
        WildAnimal wildAnimal = (WildAnimal) map.cage(new Cell(x, y));
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
        for (Upgradable upgradable : upgradables) {
            if (upgradable.getName().equals(type)) {
                if (money >= upgradable.getUpgradeCost()) {//TODO check level is less than max level
                    money -= upgradable.getUpgradeCost();
                    upgradable.upgrade();
                }
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
        for (Vehicle vehicle : vehicles) {
            if (vehicle.turn()) {
                money += vehicle.getResultMoney();
                for (Entity entity : vehicle.getResultItems()) {
                    map.addEntity(entity);
                }
            }
        }
        for (Workshop workshop : workshops) {
            workshop.turn();
        }
        currentTurn++;
        if (currentTurn % 60 == 0) {
            if (Math.random() > 0.5) {
                map.addEntity(Entity.getNewEntity("Bear"));
            } else {
                map.addEntity(Entity.getNewEntity("Lion"));
            }
        }
        //TODO ye seri chiza bayad random spawn shan??
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

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
