public class Dog extends Animal{
    Dog(){
        super("Dog");
    }
    Dog(Cell cell){
        super("Dog", cell);
    }
    void move(){
        Map map = Game.getInstance().getMap();
        Cell targetCell = map.getClosetWildAnimal();
        if(targetCell == null){
            super.move();
        }else{
            int tX = targetCell.getX();
            int tY = targetCell.getY();
            int x = cell.getX();
            int y = cell.getY();
            if(tX > x){
                cell.setX(x+1);
            }else if(tX < x){
                cell.setX(x-1);
            }else if(tY > y){
                cell.setY(y+1);
            }else if(tY < y){
                cell.setY(y-1);
            }
        }
    }
    /// bad az collid momkene Dogemoo Destroy she!!!
    void collide(Entity entity){
        if(entity instanceof WildAnimal){
            entity.destroy();
            destroy();
        }
    }
    void turn(){
        move();
    }
}
