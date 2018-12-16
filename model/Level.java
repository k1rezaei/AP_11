import java.util.HashMap;

public class Level {
    private int goalMoney;
    private HashMap<String, Integer> goalEntity = new HashMap<>();

    public Level(int goalMoney, HashMap<String, Integer> goalEntity) {
        this.goalMoney = goalMoney;
        this.goalEntity = goalEntity;
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

}
