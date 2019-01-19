import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
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
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Upgradable> upgradables;
    private int currentTurn;
    private HashMap<String, Level> levels = new HashMap<>();
    private int catLevel;

    private Game() {
        vehicles = new ArrayList<>();
        upgradables = new ArrayList<>();
        vehicles.add(truck);
        vehicles.add(helicopter);
        upgradables.add(warehouse);
        upgradables.addAll(workshops);
        upgradables.add(well);
        upgradables.addAll(vehicles);
    }

    private Game(Saver save) {
        this.money = save.getMoney();
        this.level = save.getLevel();
        this.workshops = save.getWorkshops();
        this.helicopter = save.getHelicopter();
        this.truck = save.getTruck();
        this.well = save.getWell();
        this.warehouse = save.getWarehouse();
        this.catLevel = save.getCatLevel();
        Cell.setN(level.getN());
        Cell.setM(level.getM());
        Cat.setLevel(catLevel);
        this.currentTurn = save.getCurrentTurn();
        map = new Map();
        for (FarmAnimal farmAnimal : save.getFarmAnimals()) {
            map.addEntity(farmAnimal);
        }
        for (WildAnimal wildAnimal : save.getWildAnimals()) {
            map.addEntity(wildAnimal);
        }
        for (Dog dog : save.getDogs()) {
            map.addEntity(dog);
        }
        for (Cat cat : save.getCats()) {
            map.addEntity(cat);
        }
        for (Item item : save.getItems()) {
            map.addEntity(item);
        }
        for (Plant plant : save.getPlants()) {
            map.addEntity(plant);
        }
        vehicles = new ArrayList<>();
        upgradables = new ArrayList<>();
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
            if (game.level != null && game.checkLevel()) {
                System.out.println("Level completed");//TODO clear ?
            }
        }
    }

    public void run(String command) {
        String[] commands = command.split("(\\s)+");
        Vehicle vehicle = null;
        try {
            switch (commands[0]) {
                case "run":
                    runMap(commands[1]);
                    break;
                case "save":
                    saveGame(commands[2]);
                    break;
                case "load":
                    if (commands[1].equals("game")) {
                        loadGame(commands[2]);
                    } else {
                        loadCustom(commands[2]);
                    }
                    break;
                case "print":
                    print(commands[1]);
                    break;
                case "turn":
                    turn(Integer.parseInt(commands[1]));
                    break;
                case "buy":
                    buyAnimal(commands[2]);
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
                    startWorkshop(commands[1]);
                    break;
                case "upgrade":
                    upgrade(commands[1]);
                    break;
                case "plant":
                    addPlant(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
                    break;
                case "truck":
                    vehicle = truck;
                case "helicopter":
                    if (vehicle == null) {
                        vehicle = helicopter;
                    }
                    switch (commands[1]) {
                        case "go":
                            go(vehicle);
                            break;
                        case "clear":
                            vehicle.clear();
                            break;
                        case "add":
                            vehicle.add(commands[2], Integer.parseInt(commands[3]));
                            break;
                        default:
                            throw new RuntimeException("Invalid command");
                    }
                    break;
                default:
                    throw new RuntimeException("Invalid command");
            }
        } catch (Exception e) {
            //TODO view
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }

    public void runMap(String mapName) {
        level = levels.get(mapName);
        Cell.setN(level.getN());
        Cell.setM(level.getM());
        this.money = level.getStartMoney();
        //TODO initialize and clear
    }

    public void saveGame(String command) throws IOException {
        Gson gson = new Gson();
        OutputStream outputStream = new FileOutputStream(command);
        Formatter formatter = new Formatter(outputStream);
        formatter.format(gson.toJson(new Saver(this)));
        formatter.close();
        outputStream.close();
    }

    public void loadGame(String address) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(address));
        Saver save = gson.fromJson(reader, Saver.class);
        game = new Game(save);
    }

    public void loadCustom(String address) {
        Gson gson = new Gson();
        workshops.clear();
        for (int i = 0; i < 6; i++) {
            try {
                JsonReader reader = new JsonReader(new FileReader(address + "/workshop" + i + ".json"));
                workshops.add(gson.fromJson(reader, Workshop.class));
                workshops.get(i).setName("workshop"+i);
            } catch (Exception e) {
                workshops.clear();
                throw new RuntimeException("File not found");
            }
        }
        levels.clear();
        int i = 0;
        while (true) {
            try {
                JsonReader reader = new JsonReader(new FileReader(address + "/level" + i + ".json"));
                levels.put("level" + i, gson.fromJson(reader, Level.class));
                i++;
            } catch (Exception e) {
                break;
            }
        }
    }

    public void print(String name) {
        Vehicle vehicle = null;
        switch (name) {
            case "info":
                System.out.println(this);
                break;
            case "map":
                System.out.println(map);
                break;
            case "levels":
                for (String levelName : levels.keySet()) {
                    System.out.println(levelName + "{");
                    System.out.println(levels.get(levelName));
                    System.out.println("}");
                }
                break;
            case "warehouse":
                System.out.println(warehouse);
                break;
            case "well":
                System.out.println(well);
                break;
            case "workshops":
                for (Workshop workshop : workshops) {
                    System.out.println(workshop);
                }
                break;
            case "truck":
                vehicle = truck;
            case "helicopter":
                if (vehicle == null) {
                    vehicle = helicopter;
                }
                System.out.println(vehicle);
                break;
            default:
                throw new RuntimeException("invalid input");
        }
    }

    public void go(Vehicle vehicle) {
        if (money >= vehicle.getNeededMoney() && warehouse.getNumber(vehicle.getNeededItems()) > 0) {
            money -= vehicle.getNeededMoney();
            for (Entity entity : vehicle.getNeededItems()) {
                warehouse.remove(entity.type);
            }
            vehicle.go();
        } else {
            throw new RuntimeException("vehicle requirements not met");
        }
    }

    public void startWorkshop(String workshopName) {
        for (Workshop workshop : workshops) {
            if (workshop.getName().equals(workshopName)) {
                if (getMoney() >= workshop.getStartCost()) {
                    workshop.start();
                    money -= workshop.getStartCost();
                } else {
                    throw new RuntimeException("not enough money");
                }
            }
        }
    }

    public void buyAnimal(String name) {
        Entity animal = Entity.getNewEntity(name);
        if (!(animal instanceof Animal) || (animal instanceof WildAnimal)) {
            throw new RuntimeException("Invalid animal");
        }
        if (animal.getBuyPrice() <= money) {
            money -= animal.getBuyPrice();
            map.addEntity(animal);
        } else {
            throw new RuntimeException("not enough money");
        }
    }

    public void addPlant(int x, int y) {
        //TODO age kamel birun bud ab kam nashe?
        if (well.getCurrentAmount() > 0) {
            well.decreaseWater();
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        Entity entity = new Plant(new Cell(x + i, y + j));
                        map.addEntity(entity);
                    } catch (Exception e) {

                    }
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
                if (money >= upgradable.getUpgradeCost() && upgradable.canUpgrade()) {
                    upgradable.upgrade();
                    money -= upgradable.getUpgradeCost();
                } else {
                    throw new RuntimeException("not enough money");
                }
            }
        }
        if (type.equals("cat")) {
            if (money >= Cat.getUpgradeCost()) {
                Cat.upgrade();
                money -= Cat.getUpgradeCost();
                catLevel = Cat.getLevel();
            } else {
                throw new RuntimeException("not enough money");
            }
        }
    }

    public void addEntity(Entity entity) {
        map.addEntity(entity);
    }

    public boolean checkLevel() {
        boolean result = money >= level.getGoalMoney();
        for (String name : level.getGoalEntity().keySet()) {
            result &= level.getNumber(name) <= warehouse.getNumber(name) + map.getNumber(name);
        }
        return result;
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
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Money: ").append(money).append("\n");
        stringBuilder.append("Time elapsed: ").append(currentTurn).append("\n");
        if (level != null) {
            stringBuilder.append("Required Money: ").append(level.getGoalMoney()).append("\n");
            for (String needed : level.getGoalEntity().keySet()) {
                stringBuilder.append(needed).append("{\n");
                stringBuilder.append("Needed : ").append(level.getNumber(needed)).append("\n");
                stringBuilder.append("Available: ").append(warehouse.getNumber(needed) + map.getNumber(needed)).append("\n");
                stringBuilder.append("}\n");
            }
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
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

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getCatLevel() {
        return catLevel;
    }
}