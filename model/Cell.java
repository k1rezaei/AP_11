public class Cell {
    private static int n, m;
    private int x, y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cell)) {
            return false;
        }
        Cell other = (Cell) object;
        return this.x == other.x && this.y == other.y;
    }

    public boolean isInside() {
        return x >= 0 && y >= 0 && x < n && y < m;
    }

    public void moveCloser(Cell targetCell) {
        int tX = targetCell.getX();
        int tY = targetCell.getY();
        int x = this.getX();
        int y = this.getY();
        if(tX > x){
            this.setX(x+1);
        }else if(tX < x){
            this.setX(x-1);
        }else if(tY > y){
            this.setY(y+1);
        }else if(tY < y){
            this.setY(y-1);
        }
    }
}
