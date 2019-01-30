import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Scoreboard {
    View view;
    Group root = new Group();
    Label rank = new Label();
    VBox vBox = new VBox();
    Client client;
    static private final int WIDTH = 200;
    ScrollPane scrollPane = new ScrollPane();

    Scoreboard(View view, Client client) {
        this.view = view;
        this.client = client;
        ImageView bg = new ImageView(new Image("file:textures/multiplayer/cup2.jpg"));
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        root.getChildren().add(bg);
        vBox.setSpacing(20);
        Label back = new Label("BACK");
        back.relocate(20, 10);
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.goBack();
            }
        });
        root.getChildren().add(back);
        back.setId("label_button");

        scrollPane.setId("null");
        scrollPane.relocate(100, 100);
        scrollPane.setContent(vBox);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().add(scrollPane);

    }

    public void setContent(Person[] persons) {

        for (int i = 0; i < persons.length; i++) {
            for (int j = 0; j < persons.length - 1; j++) {
                if (Integer.parseInt(persons[j].getLevel()) < Integer.parseInt(persons[j + 1].getLevel())) {
                    Person temp = persons[j];
                    persons[j] = persons[j + 1];
                    persons[j + 1] = temp;
                }
            }
        }
        vBox.getChildren().clear();
        for (int i = 0; i < persons.length; i++) {
            BorderPane borderPane = new BorderPane();
            borderPane.setMinWidth(WIDTH);
            Label id = new Label(persons[i].getName());
            Label rank = new Label(persons[i].getLevel());
            int finalI = i;
            id.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    client.getPerson(persons[finalI].getId());
                }
            });
            id.setId("rank_name");
            rank.setId("rank_level");
            borderPane.setLeft(id);
            borderPane.setRight(rank);
            vBox.setSpacing(25);
            vBox.getChildren().add(borderPane);
            vBox.setId("rank");
        }
    }


    Group getRoot() {
        return root;
    }
}
