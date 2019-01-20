import java.util.ArrayList;
import java.util.HashMap;

public class Level {
    private int startMoney;
    private int goalMoney;
    private HashMap<String, Integer> goalEntity;
    private int n, m;
    private ArrayList<String> itemList;
    //TODO Initial Entities
    public Level(int n, int m, int startMoney, int goalMoney, HashMap<String, Integer> goalEntity) {
        this.goalMoney = goalMoney;
        this.goalEntity = goalEntity;
        this.n = n;
        this.m = m;
        this.startMoney = startMoney;
    }

    public int getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(int startMoney) {
        this.startMoney = startMoney;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getGoalMoney() {
        return goalMoney;
    }

    public void setGoalMoney(int goalMoney) {
        this.goalMoney = goalMoney;
    }

    public HashMap<String, Integer> getGoalEntity() {
        return goalEntity;
    }

    public void setGoalEntity(HashMap<String, Integer> goalEntity) {
        this.goalEntity = goalEntity;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Required Money: ").append(getGoalMoney()).append("\n");
        for (String needed : getGoalEntity().keySet()) {
            stringBuilder.append(needed).append(": ").append(getNumber(needed)).append("\n");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public int getNumber(String item) {
        if (goalEntity.get(item) == null) {
            return 0;
        } else {
            return goalEntity.get(item);
        }
    }

    public void setItemList(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<String> getItemList() {
        return itemList;
    }
}
