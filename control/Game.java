
public class Game {
    static Game game = new Game();
    private Map map = new Map();

    public static Game getInstance() {
        return game;
    }

    private Game() { }

    public Map getMap() {
        return map;
    }

}
