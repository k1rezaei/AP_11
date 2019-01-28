import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Inbox {
    private Client client;
    private View view;
    private Group root = new Group();
    private final HBox columns;

    public Inbox(Client client, View view) {
        this.client = client;
        this.view = view;
        ScrollPane scrollPane = new ScrollPane();
        columns = new HBox();
        scrollPane.vvalueProperty().bind(columns.heightProperty());
        root.getChildren().add(scrollPane);
        VBox senderNames = new VBox();
        VBox recipientNames = new VBox();
        VBox messages = new VBox();
        senderNames.getChildren().add(new Label("Sender"));
        recipientNames.getChildren().add(new Label("Recipient"));
        messages.getChildren().add(new Label("Message"));
        columns.getChildren().addAll(senderNames, recipientNames, messages);
    }

    public void setContent(Talk[] talks) {
        VBox senderNames = new VBox();
        VBox recipientNames = new VBox();
        VBox messages = new VBox();
        senderNames.getChildren().add(new Label("Sender"));
        recipientNames.getChildren().add(new Label("Recipient"));
        messages.getChildren().add(new Label("Message"));
        for (Talk talk : talks) {
            Label senderName = new Label(talk.getSender().getName());
            Label recipientName = new Label(talk.getRecipient().getName());
            Label message = new Label(talk.getText());
            senderNames.getChildren().add(senderName);
            recipientNames.getChildren().add(recipientName);
            messages.getChildren().add(message);
        }
        columns.getChildren().addAll(senderNames, recipientNames, messages);
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }
}
