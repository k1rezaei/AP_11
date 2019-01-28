import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Set;


public class ViewProfile {
    private View view;
    private Client client;
    private Group root = new Group();
    final private static int CNT = 5;
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

    Label getBack() {
        return labels[4];
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

    EventHandler<MouseEvent> getEvent(Set<Person> persons){
        return  new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox f = new VBox();
                for(Person per : persons){
                    Label label = new Label(per.getName());
                    label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            view.setRoot(new ViewProfile(view, client, per).getRoot());
                        }
                    });
                    f.getChildren().add(label);
                }
                Pop pop = new Pop(f,view.getSnap(), root);

            }
        };
    }

    ViewProfile(View view, Client client, Person person) {
        this.view = view;
        this.client = client;
        this.person = person;
        for (int i = 0; i < CNT; i++) {
            labels[i] = new Label();
            labels[i].setId("label_button");
        }
        labels[0].setText("Following");
        labels[1].setText("Followers");
        labels[2].setText("Inbox");
        labels[3].setText("Friends");
        labels[4].setText("Back");
        Label name = new Label("Nickname : " + person.getName());
        Label id = new Label("ID : " + person.getId());
        Label level = new Label("Level : " + person.getLevel());
        VBox vBox = new VBox();
        vBox.getChildren().addAll(name, id, level);
        vBox.getChildren().addAll(labels);
        if (!person.getId().equals(client.getMyId())) vBox.getChildren().remove(getInbox());
        root.getChildren().add(vBox);
        getFollowers().setOnMouseClicked(getEvent(person.getFollowers()));

        getFollowing().setOnMouseClicked(getEvent(person.getFollowings()));

        getFriends().setOnMouseClicked(getEvent(person.getFriends()));

        getInbox().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                show(new Inbox(client, view).getRoot());
            }
        });
        getBack().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.goBack();
            }
        });

    }

    public Group getRoot() {
        return root;
    }
}
