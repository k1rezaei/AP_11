import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class ViewProfile {
    private View view;
    private Client client;
    private Group root = new Group();
    final private static int CNT = 8;
    private Label[] labels = new Label[CNT];
    private static final int POS_X = 270;
    public static final int POS_Y = 2;
    public static final int BOX_WIDTH = 600;
    public static final int BOX_HEIGHT = 500;
    Person person;

    Label getFollowing() {
        return labels[0];
    }

    Label getFollowers() {
        return labels[1];
    }

    Label getInbox() {
        return labels[2];
    }

    Label getPrivateMessage() {
        return labels[3];
    }

    Label getAddFriend() {
        return labels[4];
    }

    Label getFriends() {
        return labels[5];
    }

    Label getBear() {
        return labels[6];
    }

    Label getBack() {
        return labels[7];
    }

    /*private void show(Group group) {
        Pop pop = new Pop(group, view.getSnap());
        root.getChildren().add(pop.getStackPane());
        pop.getDisabler().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(pop.getStackPane());
            }
        });
    }*/

    private void show(Node node) {
        Pop pop = new Pop(node, view.getSnap(), root, Pop.AddType.WINDOW);
    }

    EventHandler<MouseEvent> getEvent(ArrayList<String> persons) {
        return event -> {
            VBox f = new VBox();
            f.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
            for (String id : persons) {
                Label label = new Label(id);
                label.setOnMouseClicked(event1 -> client.getPerson(id));
                f.getChildren().add(label);
            }
            Label label = new Label("Back");
            label.setId("label_button");
            f.getChildren().add(label);
            Pop pop = new Pop(f, view.getSnap(), root, Pop.AddType.WINDOW);

            label.setOnMouseClicked(event12 -> root.getChildren().remove(pop.getStackPane()));
        };
    }

    ViewProfile(View view, Client client, Person person) {
        System.err.println();
        System.err.println(person.getId());
        System.err.println(person.getInbox().size());

        this.view = view;
        this.client = client;
        this.person = person;
        ImageView bg = new ImageView(new Image("file:textures/multiplayer/gb.png"));
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        root.getChildren().add(bg);
        for (int i = 0; i < CNT; i++) {
            labels[i] = new Label();
            labels[i].setId("label_button");
        }

        labels[0].setText("Following");
        labels[1].setText("Followers");
        labels[2].setText("Inbox");
        labels[3].setText("Send Message");
        labels[4].setText("Add Friend");
        labels[5].setText("Friends");
        labels[6].setText("Send Bear");
        labels[7].setText("Back");
        Label name = new Label("Nickname : " + person.getName());
        Label id = new Label("ID : " + person.getId());
        Label level = new Label("Level : " + person.getLevel());

        VBox vBox = new VBox();
        vBox.relocate(POS_X, POS_Y);
        vBox.setId("prof_menu");
        vBox.getChildren().addAll(name, id, level);
        vBox.getChildren().addAll(labels);
        System.err.println("You're here");
        System.err.println(person.getFriends());
        System.err.println(person.getFollowings());
        System.err.println(person.getFollowers());
        System.err.println(person.getInbox());
        if (!person.getId().equals(client.getMyId())) {
            vBox.getChildren().remove(getInbox());
            vBox.getChildren().remove(getFollowers());
            vBox.getChildren().remove(getFollowing());
            vBox.getChildren().remove(getFriends());
        }
        System.err.println("HOHOH0");
        if (person.getId().equals(client.getMyId())) {
            vBox.getChildren().remove(getPrivateMessage());
            vBox.getChildren().remove(getAddFriend());
            vBox.getChildren().remove(getBear());
        }
        System.err.println("what about here");
        root.getChildren().add(vBox);
        System.err.println("and here");
        getFollowers().setOnMouseClicked(event -> popFollowers());
        getFollowing().setOnMouseClicked(getEvent(person.getFollowings()));
        getAddFriend().setOnMouseClicked(mouseEvent -> {
            client.addFriendRequest(person.getId());
            new Pop(new Label("Friend Request Sent"), view.getSnap(), root, Pop.AddType.ALERT);
        });
        getPrivateMessage().setOnMouseClicked(mouseEvent -> {
            HBox message = new HBox();
            TextField textField = new TextField();
            textField.setId("inputBox");
            textField.setPromptText("message...");
            Label send = new Label("Send");
            send.setId("label_button_small");
            send.setOnMouseClicked(sendEvent -> {
                client.sendPrivateMessage(person.getId(), textField.getText());
                textField.setText("");
            });
            textField.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    client.sendPrivateMessage(person.getId(), textField.getText());
                    textField.setText("");
                }
            });
            message.getChildren().addAll(textField, send);
            message.setMaxWidth(600);
            message.setMaxHeight(200);
            show(message);
        });
        getFriends().setOnMouseClicked(getEvent(person.getFriends()));
        getInbox().setOnMouseClicked(event -> show(client.getInbox().getRoot()));
        getBack().setOnMouseClicked(event -> view.goBack());
        getBear().setOnMouseClicked(mouseEvent -> client.addBear(person.getId()));
    }

    private void popFollowers() {
        VBox followersBox = new VBox();
        followersBox.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        for (String followerId : person.getFollowers()) {
            HBox hBox = new HBox();
            Label label = new Label(followerId);
            label.setOnMouseClicked(mouseEvent -> client.getPerson(followerId));
            label.setOnMouseClicked(event1 -> client.getPerson(followerId));
            Label accept = new Label("Accept");
            accept.setId("label_button_small");
            hBox.setSpacing(30);
            hBox.getChildren().addAll(label, accept);
            followersBox.getChildren().add(hBox);
            accept.setOnMouseClicked(mouseEvent -> {
                client.acceptFriendRequest(followerId);
                //TODO comment out refresh?
                view.goBack();
                client.getPerson(person.id);
            });
        }
        Label back = new Label("back");
        back.setId("label_button");
        followersBox.getChildren().add(back);
        Pop pop = new Pop(followersBox, view.getSnap(), root, Pop.AddType.WINDOW);
        back.setOnMouseClicked(mouseEvent -> root.getChildren().remove(pop.getStackPane()));
    }

    public Group getRoot() {
        return root;
    }

    public Person getPerson() {
        return person;
    }
}
