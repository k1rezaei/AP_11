import java.util.ArrayList;

public class TeamGame {
    private String player1, player2;
    private ArrayList<String> goals = new ArrayList<>();
    private int reward = 0;
    private ArrayList<Talk> messages = new ArrayList<>();

    public TeamGame(String player1, String player2, ArrayList<String> goals, int reward) {
        this.player1 = player1;
        this.player2 = player2;
        this.goals = goals;
        this.reward = reward;

    }

    public int getReward() {
        return reward;
    }

    public void remove(String goal) {
        goals.remove(goal);
    }

    public ArrayList<String> getGoals() {
        return goals;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TeamGame)) return false;
        if (((TeamGame) obj).player1.equals(player1) && ((TeamGame) obj).player2.equals(player2)) return true;
        if (((TeamGame) obj).player1.equals(player2) && ((TeamGame) obj).player2.equals(player1)) return true;
        return false;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setGoals(ArrayList<String> goals) {
        this.goals = goals;
    }

    public boolean hasPlayer(String playerId) {
        return player1.equals(playerId) || player2.equals(playerId);
    }

    public void addMessage(Talk talk) {
        messages.add(talk);
    }

    public ArrayList<Talk> getMessages() {
        return messages;
    }
}
