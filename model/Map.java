import javax.management.relation.RoleUnresolved;
import java.util.ArrayList;

public class Map {
    private ArrayList<Entity> entities = new ArrayList<Entity>();

    void relax(){
        for(int i = entities.size()-1; i >= 0; i--){
            if(entities.get(i).cell == null) entities.remove(i);
        }
    }

    void turn(){
        relax();
        for(Entity entity : entities) entity.turn();
    }

    Cell getCloset(String type, Cell cell){
        if(cell == null) throw new RuntimeException("cell is null");
        if(type == null) throw new RuntimeException("type is null");
        Cell res = null;
        for(Entity entity : entities) if(entity.getClass().getName().equalsIgnoreCase(type)){
            if(entity.getCell() == null) continue;;
            if(res == null) res = entity.getCell();
            if(cell.getDistance(res) > cell.getDistance(entity.getCell())) res = entity.getCell();
        }
        return res;
    }
    Cell getRandom(){
        return new Cell((int)Math.random()*Cell.getN(),(int)Math.random()*Cell.getM());
    }
    void addEntity(Entity entity){
        entities.add(entity);
    }
    void addEntity(Cell cell, Entity entity){
        entity.setCell(cell);
        addEntity(entity);
    }
    Entity cage(Cell cell){
        for(Entity entity : entities)if(entity.getCell() != null){
            if(entity.getCell().equals(cell) && entity instanceof WildAnimal){
                entity.setCell(null);
                return entity;
            }
        }
        return null;
    }
    Item pickUp(Cell cell){
        for(Entity entity : entities) if(cell.equals(entity.getCell()) && entity instanceof Item){
            entity.setCell(null);
            return (Item)entity;
        }
        return null;
    }
    /// to do upgrade
}

