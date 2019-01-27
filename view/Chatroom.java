import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


public class Chatroom {
    private View view;
    private Group root = new Group();
    private Label content = new Label();
    private Label send = new Label();
    private TextField textField = new TextField();

    public Chatroom(View view) {
        this.view = view;
        content.setId("chatBox");
        send.setId("label_button");
        send.setText("send");
        send.relocate(200,200);
        content.relocate(300,300);
        send.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Game.getInstance().getClient().addText(textField.getText());
                textField.setText("");
            }
        });
        root.getChildren().addAll(content, send, textField);
    }

    public void setContent(String input){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                content.setText(input);
            }
        });
    }

    public Group getRoot() {
        return root;
    }
}
