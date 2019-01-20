import java.util.ArrayList;

public class Cell {
    private static int n, m;
    private static int[][] d = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    private int x, y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        if (!isInside()) {
            throw new RuntimeException("Cell out of bounds");
        }
    }

    public static Cell getRandomCell() {
        return new Cell((int) (Math.random() * Cell.getN()), (int) (Math.random() * Cell.getM()));
    }

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        Cell.n = n;
    }

    public static int getM() {
        return m;
    }

    public static void setM(int m) {
        Cell.m = m;
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

    int move(Cell targetCell) {
        int targetX = targetCell.getX();
        int targetY = targetCell.getY();
        ArrayList<Integer> possible = new ArrayList<>();
        if (targetX > getX()) {
            possible.add(1);
        }
        if (targetX < getX()) {
            possible.add(3);
        }
        if (targetY > getY()) {
            possible.add(2);
        }
        if (targetY < getY()) {
            possible.add(0);
        }
        if (!possible.isEmpty()) {
            int index = possible.get(0);//(int) (Math.random() * possible.size())); //TODO MAKE RANDOM
            x += d[index][0];
            y += d[index][1];
            return index;
        }
        return 0;
    }

    int getDistance(Cell cell) {
        return Math.abs(cell.getX() - x) + Math.abs(cell.getY() - y);
    }

}
