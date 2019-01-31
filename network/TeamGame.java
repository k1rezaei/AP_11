import java.util.ArrayList;

public class TeamGame {
    String p1, p2;
    ArrayList<String> goals = new ArrayList<>();

    public TeamGame(String p1, String p2, ArrayList<String> goals) {
        this.p1 = p1;
        this.p2 = p2;
        this.goals = goals;
    }

    public void remove(String goal) {
        for (String item : goals) {
            if(item.equals(goal)) {
                goals.remove(goal);
                return ;
            }
        }
    }

    public ArrayList<String> getGoals() {return goals;}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TeamGame)) return false;
        if(((TeamGame) obj).p1.equals(p1) && ((TeamGame) obj).p2.equals(p2)) return true;
        if(((TeamGame) obj).p1.equals(p2) && ((TeamGame) obj).p2.equals(p1)) return true;
        return false;
    }
}
