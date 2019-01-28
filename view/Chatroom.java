import javafx.application.Platform;
import javafx.event.EventHandler;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


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
        send.setId("label_button");
        ImageView temp = new ImageView(new Image("file:textures/multiplayer/send.png"));
        temp.setFitWidth(60);
        temp.setFitHeight(30);
        send.setGraphic(temp);
        send.relocate(200, 200);
        content.relocate(400, 0);
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

        root.getChildren().addAll(scrollPane, send, textField);

        scrollPane.relocate(400, 300);
        scrollPane.translateXProperty().bind(scrollPane.widthProperty().divide(2).negate());
        scrollPane.translateYProperty().bind(scrollPane.heightProperty().divide(2).negate());


    }


    public void setContent(Talk[] talks) {
        for(int i = 0; i < talks.length; i++){
            HBox hBox = new HBox();
            Label sender = new Label(talks[i].getSender().getName());
            Label text = new Label(talks[i].getText());
            hBox.getChildren().addAll(sender, text);
            content.getChildren().add(hBox);
        }
    }

    public Group getRoot() {
        return root;
    }
}
