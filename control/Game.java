
public class Game {
    static Game game = new Game();
    private Map map = new Map();
    private Warehouse warehouse;

    public static Game getInstance() {
        return game;
    }

    private Game() { }

    public Map getMap() {
        return map;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }
}
