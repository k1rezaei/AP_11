abstract public class Animal extends Entity {
    static private int[] dx = {+1,0,-1,0};
    static private int[] dy = {0,+1,0,-1};
    Animal(String type){
        super(type);
    }
    Animal(String type, Cell cell){
        super(type,cell);
    }
    void move(){
        while(true){
            int dir = (int)(Math.random()*4);
            cell.setX(cell.getX() + dx[dir]);
            cell.setY(cell.getY() + dy[dir]);
            if(cell.isInside()){
                break;
            }
            cell.setX(cell.getX() - dx[dir]);
            cell.setY(cell.getY() - dy[dir]);
        }
    }
    abstract void collide(Entity entity);
    abstract void turn();
}
