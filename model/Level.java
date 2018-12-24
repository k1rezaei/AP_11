import java.util.HashMap;

public class Level {
    private int goalMoney;
    private HashMap<String, Integer> goalEntity = new HashMap<>();
    private int n,m;
    //TODO Initial Entities
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
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Required Money:\n");
        stringBuilder.append(getGoalMoney()+"\n");
        for(String needed:getGoalEntity().keySet()){
            stringBuilder.append(needed).append(":\n");
            stringBuilder.append("Needed : ").append(getNumber(needed)).append("\n");//TODO khode level get dashte bashe
        }
        return stringBuilder.toString();
    }
    public int getNumber(String item){
        if (goalEntity.get(item)==null){
            return 0;
        }
        else{
            return goalEntity.get(item);
        }
    }
}
