import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Set;


public class ViewProfile {
    private View view;
    private Client client;
    private Group root = new Group();
    final private static int CNT = 7;
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

    Label getPrivateMessage() {return labels[3];}

    Label getAddFriend() {return labels[4];}

    Label getFriends() {
        return labels[5];
    }

    Label getBack() {
        return labels[6];
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
        Pop pop = new Pop(node, view.getSnap());
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
        labels[3].setText("Private Message");
        labels[4].setText("Add Friend");
        labels[5].setText("Friends");
        labels[6].setText("Back");
        Label name = new Label("Nickname : " + person.getName());
        Label id = new Label("ID : " + person.getId());
        Label level = new Label("Level : " + person.getLevel());
        VBox vBox = new VBox();
        vBox.getChildren().addAll(name, id, level);
        vBox.getChildren().addAll(labels);
        if (!person.getId().equals(client.getMyId())) vBox.getChildren().remove(getInbox());
        if(person.getId().equals(client.getMyId())){
            vBox.getChildren().remove(getPrivateMessage());
            vBox.getChildren().remove(getAddFriend());
        }
        root.getChildren().add(vBox);
        getFollowers().setOnMouseClicked(getEvent(person.getFollowers()));

        getFollowing().setOnMouseClicked(getEvent(person.getFollowings()));

        getAddFriend().setOnMouseClicked(mouseEvent -> client.addFriendRequest(person.getId()));

        getPrivateMessage().setOnMouseClicked(mouseEvent -> {
            HBox message = new HBox();
            TextField textField = new TextField();
            textField.setPromptText("message...");
            Label send = new Label("Send");
            send.setId("label_button_small");
            send.setOnMouseClicked(sendEvent -> {
                client.sendPrivateMessage(person.getId(),textField.getText());
                textField.setText("");
            });
            textField.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                    client.sendPrivateMessage(person.getId(), textField.getText());
                    textField.setText("");
                }
            });
            message.getChildren().addAll(textField,send);
            message.setMaxWidth(600);
            message.setMaxHeight(200);
            show(message);
        });
        getFriends().setOnMouseClicked(getEvent(person.getFriends()));

        getInbox().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                show(client.getInbox().getRoot());
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
