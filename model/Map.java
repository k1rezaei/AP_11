import java.util.ArrayList;

public class Map {
    final private static int BASE_DISTANCE = 6;
    private ArrayList<Entity> entities = new ArrayList<Entity>();

    void relax() {
        for (int i = entities.size() - 1; i >= 0; i--) {
            if (entities.get(i).getCell() == null) entities.remove(i);
        }
    }

    void turn() {
        for (Entity entity : entities) entity.turn();
        for (Entity entity : entities)
            for (Entity entity2 : entities)
                if (entity != entity2 && entity instanceof Animal) {
                    if (entity.getCell() == null || entity2.getCell() == null) continue;
                    if (entity.getCell().getDistance(entity2.getCell()) <= BASE_DISTANCE) {
                        ((Animal) entity).collide(entity2);
                    }
                }
        relax();
        for(Entity entity : entities){
            if(entity instanceof FarmAnimal){
                Item item = ((FarmAnimal) entity).produce();
                if(item != null){
                    addEntity(item);
                }
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

    void addEntity(Entity entity) {
        if(entity.getCell() == null){
            entity.setCell(Cell.getRandomCell());
        }
        if(!entity.getCell().isInside()) throw new RuntimeException("Cell is not inside");
        entities.add(entity);
    }

    void addEntity(Cell cell, Entity entity) {
        if(cell == null) throw new RuntimeException("Cell is null");
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

    @Override
    public String toString(){
        String res = new String();
        for(Entity entity : entities) if(entity.getCell() != null){
            res += (entity.type + " : " + entity.getCell().getX() + " " + entity.getCell().getY() + "\n");
        }
        return res.substring(0,res.length()-1);
    }
}

