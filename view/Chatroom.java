import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class Chatroom {
    private View view;
    private Client client;
    private Group root = new Group();
    private VBox content = new VBox();
    private Label send = new Label();
    private TextField textField = new TextField();

    public Chatroom(View view,Client client) {
        this.view = view;
        this.client = client;
        content.setId("chatBox");
        send.setId("label_button");
        send.setText("send");
        send.relocate(200,200);
        content.relocate(300,300);
        send.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.addMessageToChatRoom(textField.getText());
                textField.setText("");
            }
        });
        root.getChildren().addAll(content, send, textField);
    }

    Label getLabel(String input){
        return null;
    }

    public void setContent(String input){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] inputs = input.split("\n");
                content.getChildren().clear();
                for(int i = 0; i < inputs.length; i++){

                }
                //content.setText(input);
            }
        });
    }

    public Group getRoot() {
        return root;
    }
}
