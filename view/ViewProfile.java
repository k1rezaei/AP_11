import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class ViewProfile {
    private View view;
    private Client client;
    private Group root;
    final private static int CNT = 4;
    private Label[] labels = new Label[CNT];
    Person person;

    Label getFollowers() {
        return labels[0];
    }

    Label getFollowing() {
        return labels[1];
    }

    Label getInbox() {
        return labels[2];
    }

    Label getFriends() {
        return labels[3];
    }

    private void show(Group group) {
        Pop pop = new Pop(group, view.getSnap());
        root.getChildren().add(pop.getStackPane());
        pop.getDisabler().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(pop.getStackPane());
            }
        });

    }

    ViewProfile(View view, Client client, Person person) {
        this.view = view;
        this.client = client;
        this.person = person;
        for (int i = 0; i < CNT; i++) {
            labels[i] = new Label();
            labels[i].setId("label_text");
        }
        labels[0].setText("Following");
        labels[1].setText("Followers");
        labels[2].setText("Inbox");
        labels[3].setText("Friends");
        VBox vBox = new VBox(labels);
        root.getChildren().add(vBox);
        getFollowers().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

        getFollowing().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

        getFriends().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

        getInbox().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                show(new Inbox(client, view).getRoot());
            }
        });
    }

    public Group getRoot() {
        return root;
    }
}
