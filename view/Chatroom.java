import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Chatroom {
    private View view;
    private Client client;
    private Group root = new Group();
    private VBox content = new VBox();
    private Label send = new Label();
    private TextField textField = new TextField();
    private TextField reply = new TextField();
    private boolean replied = false;
    private ImageView bg = new ImageView(new Image("file:textures/multiplayer/chat.jpg"));

    {
        bg.setFitHeight(600);
        bg.setFitWidth(800);
        root.getChildren().add(bg);

    }

    final private static int HEIGHT = 400;
    final private static int WIDTH = 600;

    public Chatroom(View view, Client client) {
        this.view = view;
        this.client = client;

        Label back = new Label("BACK");
        back.setId("label_button");
        ImageView temp2 = new ImageView(new Image("file:textures/back_button.png"));
        temp2.relocate(20, 20);
        //back.setGraphic(temp2);
        send.setId("label_button_small");
        ImageView temp = new ImageView(new Image("file:textures/multiplayer/send.png"));
        temp.setFitWidth(60);
        temp.setFitHeight(30);
        send.setText("SEND");
        //send.setGraphic(temp);
        send.setOnMouseClicked(event -> sendMessage());
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) sendMessage();
        });

        back.setOnMouseClicked(event -> view.setRoot(client.getMultiPlayerMenu().getRoot()));
        textField.relocate(400 - WIDTH / 2, 300 + HEIGHT / 2);
        textField.setMinSize(WIDTH - 60, 30);
        textField.setMaxSize(WIDTH - 60, 30);
        reply.relocate(400 - WIDTH / 2, 350 + HEIGHT / 2);
        reply.setMinSize(WIDTH - 60, 30);
        reply.setMaxSize(WIDTH - 60, 30);
        send.relocate(400 + WIDTH / 2 - 60, 300 + HEIGHT / 2);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(content);

        scrollPane.setMaxHeight(HEIGHT);
        scrollPane.setMaxWidth(WIDTH);
        scrollPane.setMinHeight(HEIGHT);
        scrollPane.setMinWidth(WIDTH);
        reply.setDisable(true);

        scrollPane.relocate(400, 300);
        scrollPane.translateXProperty().bind(scrollPane.widthProperty().divide(2).negate());
        scrollPane.translateYProperty().bind(scrollPane.heightProperty().divide(2).negate());

        content.setId("chat");

        content.setSpacing(20);
        scrollPane.vvalueProperty().bind(content.heightProperty());
        Label clear = setUpClear();
        root.getChildren().addAll(scrollPane, send, textField, back,reply,clear);
    }

    private Label setUpClear() {
        Label clear = new Label("Clear Reply");
        clear.relocate(400 + WIDTH / 2 - 60, 350 + HEIGHT / 2);
        clear.setId("label_button_small");
        clear.setOnMouseClicked(mouseEvent -> clearReply());
        return clear;
    }

    private void sendMessage() {
        if(!replied) client.addMessageToChatRoom(textField.getText());
        else client.addMessageToChatRoom(textField.getText(), reply.getText());
        clearReply();
        textField.setText("");
    }

    private void clearReply() {
        replied = false;
        reply.setText("");
    }


    public void setContent(Talk[] talks) {
        content.getChildren().clear();
        for (int i = 0; i < talks.length; i++) {
            VBox messageVBox = new VBox();
            messageVBox.setSpacing(20);
            HBox messageHBox = new HBox();
            messageHBox.setStyle("-fx-alignment: center-left");
            Label sender = new Label(talks[i].getSender());
            sender.setId("sender");
            Label text = new Label(talks[i].getText());
            text.setId("message");
            messageHBox.setSpacing(25);
            messageHBox.setMinWidth(WIDTH);
            messageHBox.setMaxWidth(WIDTH);
            messageHBox.getChildren().addAll(sender, text);
            text.setOnMouseClicked(mouseEvent -> {
                replied = true;
                reply.setText(text.getText());
            });
            if (talks[i].getRepliedText() != null) {
                Label replyLabel = new Label(talks[i].getRepliedText());
                replyLabel.setId("reply");
                messageVBox.getChildren().add(replyLabel);
            }
            messageHBox.setAlignment(Pos.CENTER_LEFT);
            messageVBox.getChildren().add(messageHBox);
            content.getChildren().add(messageVBox);
        }
    }

    public Group getRoot() {
        return root;
    }
}
