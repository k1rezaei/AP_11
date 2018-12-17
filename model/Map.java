import java.util.ArrayList;

public class Map {
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    final private static int BASE_DISTANCE = 6;

    void relax() {
        for (int i = entities.size() - 1; i >= 0; i--) {
            if (entities.get(i).getCell() == null) entities.remove(i);
        }
    }

    void turn() {
        relax();
        for (Entity entity : entities) entity.turn();
        for (Entity entity : entities)
            for (Entity entity2 : entities)
                if (entity != entity2 && entity instanceof Animal) {
                    if (entity.getCell() == null || entity2.getCell() == null) continue;
                    if (entity.getCell().getDistance(entity2.getCell()) <= BASE_DISTANCE) {
                        ((Animal) entity).collide(entity2);
                    }
                }

    }

    Cell getCloset(String type, Cell cell) {
        if (cell == null) throw new RuntimeException("cell is null");
        if (type == null) throw new RuntimeException("type is null");
        Cell res = null;
        for (Entity entity : entities)
            if (entity.getClass().getName().equalsIgnoreCase(type)) {
                if (entity.getCell() == null) continue;
                if (res == null) res = entity.getCell();
                if (cell.getDistance(res) > cell.getDistance(entity.getCell())) res = entity.getCell();
            }
        return res;
    }

    Cell getRandom() {
        return new Cell((int) (Math.random() * Cell.getN()), (int) (Math.random() * Cell.getM()));
    }

    void addEntity(Entity entity) {
        entities.add(entity);
    }

    void addEntity(Cell cell, Entity entity) {
        entity.setCell(cell);
        addEntity(entity);
    }

    Entity cage(Cell cell) {
        for (Entity entity : entities)
            if (entity.getCell() != null) {
                if (entity.getCell().equals(cell) && entity instanceof WildAnimal) {
                    entity.destroy();
                    return entity;
                }
            }
        return null;
    }

    Item pickUp(Cell cell) {
        for (Entity entity : entities)
            if (cell.equals(entity.getCell()) && entity instanceof Item) {
                entity.destroy();
                return (Item) entity;
            }
        return null;
    }
    /// to do upgrade
}

