import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Scoreboard {
    View view;
    Group root = new Group();
    Label rank = new Label();
    VBox vBox = new VBox();
    Client client;

    ScrollPane scrollPane = new ScrollPane();

    Scoreboard(View view, Client client) {
        this.view = view;
        this.client = client;
        vBox.setSpacing(20);
        Label back = new Label();
        back.relocate(10, 10);
        back.setGraphic(new ImageView(new Image("file:textures/back_button.png")));
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.goBack();
            }
        });
        root.getChildren().add(back);
        back.setId("label_button");

        scrollPane.relocate(400, 50);
        scrollPane.translateXProperty().bind(scrollPane.widthProperty().divide(2).negate());
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
        System.err.println(persons.length);
        vBox.getChildren().clear();
        for (int i = 0; i < persons.length; i++) {
            HBox hBox = new HBox();
            Label id = new Label(persons[i].getId());
            Label rank = new Label(persons[i].getLevel());
            int finalI = i;
            id.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    view.setRoot(new ViewProfile(view, client, persons[finalI]).getRoot());
                }
            });
            id.setId("sender");
            rank.setId("message");
            hBox.getChildren().addAll(id, rank);
            hBox.setSpacing(25);
            vBox.getChildren().add(hBox);
        }
    }


    Group getRoot() {
        return root;
    }
}
