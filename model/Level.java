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

    public ArrayList<String> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Level)) return false;
        Level oth = (Level) obj;
        if (this.m != oth.getM()) return false;
        if (this.n != oth.getN()) return false;
        if (this.goalMoney != oth.getGoalMoney()) return false;
        if (this.startMoney != oth.getStartMoney()) return false;
        if (!checkGoals(this.goalEntity, oth.getGoalEntity())) return false;
        return true;
    }

    private boolean checkGoals(HashMap<String, Integer> map1, HashMap<String, Integer> map2) {
        for (String str : map1.keySet()) {
            if (map2.get(str) == null || map2.get(str) != map1.get(str)) return false;
        }
        for (String str : map2.keySet()) {
            if (map1.get(str) == null || map1.get(str) != map2.get(str)) return false;
        }
        return true;
    }
}
