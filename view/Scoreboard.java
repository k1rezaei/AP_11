import javafx.scene.Group;
import javafx.scene.control.Label;


public class Scoreboard {
    View view;
    Group root = new Group();
    Label rank = new Label();

    Scoreboard(View view) {
        this.view = view;
        root.getChildren().add(rank);
    }

    public void setContent(String input) {
        String[] ranks = input.split("\n");
        for (int i = 0; i < ranks.length; i++)
            for (int j = 0; j < ranks.length - 1; j++) {
                if (ranks[j].split(":")[1].compareTo(ranks[j].split(":")[1]) > 0) {
                    String temp = ranks[j];
                    ranks[j] = ranks[j + 1];
                    ranks[j + 1] = temp;
                }
            }
        String content = "";
        for (int i = 0; i < ranks.length; i++) {
            content += ranks[i] + "\n";
        }
        rank.setText(content);
    }

    Group getRoot() {
        return root;
    }
}
