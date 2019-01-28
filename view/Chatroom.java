import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.awt.*;


public class Chatroom {
    private View view;
    private Client client;
    private Group root = new Group();
    private VBox content = new VBox();
    private Label send = new Label();
    private TextField textField = new TextField();

    final private static int HEIGHT = 400;
    final private static int WIDTH = 600;

    public Chatroom(View view, Client client) {
        this.view = view;
        this.client = client;

        Label back = new Label();
        back.setId("label_button");
        ImageView temp2 = new ImageView(new Image("file:textures/back_button.png"));
        temp2.relocate(20,20);
        back.setGraphic(temp2);
        send.setId("label_button");
        ImageView temp = new ImageView(new Image("file:textures/multiplayer/send.png"));
        temp.setFitWidth(60);
        temp.setFitHeight(30);
        send.setGraphic(temp);
        send.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.addMessageToChatRoom(textField.getText());
                textField.setText("");
            }
        });
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    client.addMessageToChatRoom(textField.getText());
                    textField.setText("");
                }
            }
        });

        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.setRoot(client.getMultiPlayerMenu().getRoot());
            }
        });
        textField.relocate(400 - WIDTH / 2, 300 + HEIGHT / 2);
        textField.setMinSize(WIDTH-60,30);
        textField.setMaxSize(WIDTH-60, 30);
        send.relocate(400 + WIDTH / 2 - 60, 300 + HEIGHT / 2);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(content);

        scrollPane.setMaxHeight(HEIGHT);
        scrollPane.setMaxWidth(WIDTH);
        scrollPane.setMinHeight(HEIGHT);
        scrollPane.setMinWidth(WIDTH);
        scrollPane.setId("chatBox");

        root.getChildren().addAll(scrollPane, send, textField, back);

        scrollPane.relocate(400, 300);
        scrollPane.translateXProperty().bind(scrollPane.widthProperty().divide(2).negate());
        scrollPane.translateYProperty().bind(scrollPane.heightProperty().divide(2).negate());
        content.setSpacing(20);
        scrollPane.vvalueProperty().bind(content.heightProperty());

    }


    public void setContent(Talk[] talks) {
        content.getChildren().clear();
        for(int i = 0; i < talks.length; i++){
            HBox hBox = new HBox();
            Label sender = new Label(talks[i].getSender().getName());

            sender.setId("sender");
            Label text = new Label(talks[i].getText());
            text.setId("message");
            hBox.setSpacing(25);
            hBox.setMinWidth(WIDTH);
            hBox.setMaxWidth(WIDTH);
            hBox.getChildren().addAll(sender, text);

            content.getChildren().add(hBox);
        }
    }

    public Group getRoot() {
        return root;
    }
}
