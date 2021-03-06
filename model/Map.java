import java.util.ArrayList;

public class Map {
    final private static int BASE_DISTANCE = 30;
    private ArrayList<Entity> entities = new ArrayList<>();

    Map() {
    }

    Map(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    int getNumber(String type) {
        int cnt = 0;
        for (Entity entity : entities) if (entity.getCell() != null && entity.getType().equalsIgnoreCase(type)) cnt++;
        return cnt;
    }

    void relax() {
        for (int i = entities.size() - 1; i >= 0; i--) {
            if (entities.get(i).getCell() == null) entities.remove(i);
        }
    }

    void turn() {
        for (int i = entities.size() - 1; i >= 0; i--) if (entities.get(i).getCell() != null) entities.get(i).turn();
        for (int i = entities.size() - 1; i >= 0; i--) {
            for (int j = entities.size() - 1; j >= 0; j--) {
                Entity entity = entities.get(i);
                Entity entity2 = entities.get(j);
                if (entity != entity2 && entity instanceof Animal) {
                    if (entity.getCell() == null || entity2.getCell() == null) continue;
                    if (entity.getCell().getDistance(entity2.getCell()) <= BASE_DISTANCE) {
                        ((Animal) entity).collide(entity2);
                    }
                }
            }
        }
        for (int i = entities.size() - 1; i >= 0; i--) {
            if (entities.get(i) instanceof FarmAnimal && entities.get(i).getCell() != null) {
                Item item = ((FarmAnimal) entities.get(i)).produce();
                if (item != null) {
                    addEntity(item);
                }
            }
        }

    }

    Cell getClosest(String type, Cell cell) {
        if (cell == null) throw new RuntimeException("cell is null");
        if (type == null) throw new RuntimeException("type is null");
        Cell res = null;
        for (Entity entity : entities) {
            if (entity.getClass().getName().equalsIgnoreCase(type)) {
                if (entity.getCell() == null) continue;
                if (res == null) res = entity.getCell();
                if (cell.getDistance(res) > cell.getDistance(entity.getCell())) res = entity.getCell();
            }
        }
        return res;
    }

    void addEntity(Entity entity) {
        if (entity.getCell() == null) {
            entity.setCell(Cell.getRandomCell());
        }
        if (!entity.getCell().isInside()) throw new RuntimeException("Cell is not inside");
        entities.add(entity);
    }

    void addEntity(Cell cell, Entity entity) {
        if (cell == null) throw new RuntimeException("Cell is null");
        entity.setCell(cell);
        addEntity(entity);
    }

    Entity cage(Cell cell) {
        for (Entity entity : entities) {
            if (entity.getCell() != null) {
                if (entity.getCell().equals(cell) && entity instanceof WildAnimal) {
                    entity.destroy();
                    return entity;
                }
            }
        }
        return null;
    }

    Item pickUp(Cell cell) {
        for (Entity entity : entities) {
            if (cell.equals(entity.getCell()) && entity instanceof Item) {
                return (Item) entity;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        char[][] map = new char[100][100];
        for (int i = 0; i < 100; i++) for (int j = 0; j < 100; j++) map[i][j] = '.';
        for (Entity entity : entities)
            if (entity.getCell() != null)
                map[entity.getCell().getX()][entity.getCell().getY()] = entity.getType().charAt(0);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) System.out.print(map[i][j]);
            System.out.println();
        }

        //return "";

        String res = new String();
        for (Entity entity : entities)
            if (entity.getCell() != null) {
                res += (entity.type + " : " + entity.getCell().getX() + " " + entity.getCell().getY() + "\n");
            }
        if (res.length() == 0) return "Map is empty";
        return res.substring(0, res.length() - 1);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}

