public class Cell {
    private static int n, m;
    private int x, y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Cell getRandomCell() {
        return new Cell((int) (Math.random() * Cell.getN()), (int) (Cell.getM()));
    }

    public static int getN() {
        return n;
    }

    public static int getM() {
        return m;
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

    void move(Cell targetCell) {
        int tX = targetCell.getX();
        int tY = targetCell.getY();
        if (tX > getX()) {
            setX(x + 1);
        } else if (tX < getX()) {
            setX(x - 1);
        } else if (tY > getY()) {
            setY(y + 1);
        } else if (tY < getY()) {
            setY(y - 1);
        }
    }

    int getDistance(Cell cell) {
        return Math.abs(cell.getX() - x) + Math.abs(cell.getY() - y);
    }

}
