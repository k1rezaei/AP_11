import java.util.ArrayList;

public class Saver {
    private int money;
    private Level level;
    private ArrayList<Workshop> workshops;
    private Helicopter helicopter;
    private Truck truck;
    private Well well;
    private Warehouse warehouse;
    private int currentTurn;
    private int catLevel;
    private ArrayList<FarmAnimal> farmAnimals = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Dog> dogs = new ArrayList<>();
    private ArrayList<Cat> cats = new ArrayList<>();
    private ArrayList<WildAnimal> wildAnimals = new ArrayList<>();
    private ArrayList<Plant> plants = new ArrayList<>();

    public Saver(Game game) {
        this.money = game.getMoney();
        this.level = game.getLevel();
        this.workshops = game.getWorkshops();
        this.helicopter = game.getHelicopter();
        this.truck = game.getTruck();
        this.well = game.getWell();
        this.warehouse = game.getWarehouse();
        this.currentTurn = game.getCurrentTurn();
        this.catLevel = game.getCatLevel();
        for (Entity entity : game.getMap().getEntities()) {
            if (entity instanceof FarmAnimal) {
                farmAnimals.add((FarmAnimal) entity);
            } else if (entity instanceof WildAnimal) {
                wildAnimals.add((WildAnimal) entity);
            } else if (entity instanceof Cat) {
                cats.add((Cat) entity);
            } else if (entity instanceof Dog) {
                dogs.add((Dog) entity);
            } else if (entity instanceof Item) {
                items.add((Item) entity);
            } else {
                plants.add((Plant) entity);
            }
        }
    }

    public int getMoney() {
        return money;
    }

    public Level getLevel() {
        return level;
    }

    public ArrayList<Workshop> getWorkshops() {
        return workshops;
    }

    public Helicopter getHelicopter() {
        return helicopter;
    }

    public Truck getTruck() {
        return truck;
    }

    public Well getWell() {
        return well;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getCatLevel() {
        return catLevel;
    }

    public ArrayList<FarmAnimal> getFarmAnimals() {
        return farmAnimals;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Dog> getDogs() {
        return dogs;
    }

    public ArrayList<Cat> getCats() {
        return cats;
    }

    public ArrayList<WildAnimal> getWildAnimals() {
        return wildAnimals;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }
}
