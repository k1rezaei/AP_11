import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Inbox {
    private final HBox columns;
    private Client client;
    private View view;
    private Group root = new Group();
    private boolean initialized = false;

    public Inbox(View view, Client client) {
        this.client = client;
        this.view = view;
        ScrollPane scrollPane = new ScrollPane();
        columns = new HBox();
        scrollPane.vvalueProperty().bind(columns.heightProperty());
        scrollPane.setContent(columns);
        scrollPane.setPrefWidth(400);
        scrollPane.setPrefHeight(400);
        scrollPane.translateXProperty().bind(scrollPane.widthProperty().divide(2).negate());
        scrollPane.translateYProperty().bind(scrollPane.heightProperty().divide(2).negate());
        root.getChildren().add(scrollPane);
        setContent(new Talk[0]);
        columns.setSpacing(10);
    }

    public void setContent(Talk[] talks) {
        initialized |= talks.length > 0;
        VBox senderNames = new VBox();
        VBox recipientNames = new VBox();
        VBox messages = new VBox();
        columns.getChildren().clear();
        senderNames.getChildren().add(new Label("Sender"));
        recipientNames.getChildren().add(new Label("Recipient"));
        messages.getChildren().add(new Label("Message"));
        for (int i = talks.length - 1; i >= 0; i--) {
            Talk talk = talks[i];
            Label senderName = new Label(talk.getSender());
            Label recipientName = new Label(talk.getRecipient());
            Label message = new Label(talk.getText());
            senderNames.getChildren().add(senderName);
            recipientNames.getChildren().add(recipientName);
            messages.getChildren().add(message);
        }
        for (Node node : senderNames.getChildren()) node.setStyle("-fx-font-size : 20");
        for (Node node : recipientNames.getChildren()) node.setStyle("-fx-font-size : 20");
        for (Node node : messages.getChildren()) node.setStyle("-fx-font-size : 20");
        columns.getChildren().addAll(senderNames, recipientNames, messages);
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
