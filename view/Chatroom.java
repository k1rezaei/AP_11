import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

    public Chatroom(View view, Client client) {
        this.view = view;
        this.client = client;
        content.setId("chatBox");
        send.setId("label_button");
        send.setText("send");
        send.relocate(200, 200);
        content.relocate(300, 300);
        send.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.addMessageToChatRoom(textField.getText());
                textField.setText("");
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(content);
        root.getChildren().addAll(content, send, textField);
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
