import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;


public class Chatroom {
    final private static int HEIGHT = 400;
    final private static int WIDTH = 600;
    private static String[] emojis = {"\uD83D\uDE17", "\uD83D\uDE10", "\uD83D\uDE04", "\uD83D\uDE0D", "\uD83D\uDE09", "\uD83D\uDE33", "\uD83D\uDE14", "\uD83D\uDE02", "\uD83D\uDE22", "\uD83D\uDE28", "\uD83D\uDE20", "\uD83D\uDE0E",};
    private View view;
    private Client client;
    private Group root = new Group();
    private VBox content = new VBox();
    private Label send = new Label();
    private TextField textField = new TextField();
    private TextField reply = new TextField();
    private boolean replied = false;
    private HashMap<String, String> colors = new HashMap<>();
    private ImageView bg = new ImageView(new Image("file:textures/multiplayer/chat.jpg"));
    private Label clear;
    private FlowPane emojiSelect;

    {
        bg.setFitHeight(600);
        bg.setFitWidth(800);
        root.getChildren().add(bg);

    }

    public Chatroom(View view, Client client) {
        this.view = view;
        this.client = client;
        textField.setId("chat_field");
        reply.setId("chat_field");
        Label back = new Label("BACK");
        back.setId("label_button");
        //ImageView temp2 = new ImageView(new Image("file:textures/back_button.png"));
        back.relocate(30, 30);
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
        textField.relocate(30 + 400 - WIDTH / 2, 300 + HEIGHT / 2);
        textField.setMinSize(WIDTH - 90, 30);
        textField.setMaxSize(WIDTH - 90, 30);


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
        setUpClear();
        clearReply();
        Label emoji = setUpEmoji();
        root.getChildren().addAll(scrollPane, emoji, send, textField, back, reply, clear, emojiSelect);
    }

    private Label setUpEmoji() {
        Label emoji = new Label("\uD83D\uDE04");
        emoji.setPrefSize(30, 30);
        emoji.relocate(400 - WIDTH / 2, 300 + HEIGHT / 2);
        emoji.setId("emoji");
        setUpEmojiSelect();
        emoji.setOnMouseClicked(event -> emojiSelect.setVisible(!emojiSelect.isVisible()));
        return emoji;
    }

    private void setUpEmojiSelect() {
        emojiSelect = new FlowPane();
        emojiSelect.relocate(30 + 400 - WIDTH / 2, 300 + HEIGHT / 2 - 3 * 5 - 30 * 3);
        emojiSelect.setVgap(5);
        emojiSelect.setHgap(5);
        emojiSelect.setMaxSize(30 * 2 + 15 + 1, 5 * 3 + 30 * 4 + 1);
        emojiSelect.setStyle("-fx-background-color: rgba(200,200,200,0.8);" +
                "-fx-padding: 5;" +
                "-fx-background-radius: 10");
        for (int i = 0; i < emojis.length; i++) {
            Label emoj = new Label(emojis[i]);
            emoj.setId("emoji");
            int finalI = i;
            emoj.setOnMouseClicked(event1 -> {
                System.err.println("WTF?");
                textField.replaceText(textField.getSelection(), emojis[finalI]);
                emojiSelect.setVisible(false);
            });
            emojiSelect.getChildren().add(emoj);
        }
        emojiSelect.setVisible(false);
    }

    private void setUpClear() {
        clear = new Label("Clear");
        clear.relocate(400 + WIDTH / 2 - 60, 350 + HEIGHT / 2);
        clear.setId("label_button_small");
        clear.setOnMouseClicked(mouseEvent -> clearReply());
    }

    private void sendMessage() {
        if (!replied) client.addMessageToChatRoom(textField.getText());
        else client.addMessageToChatRoom(textField.getText(), reply.getText());
        clearReply();
        textField.setText("");
    }


    private void clearReply() {
        replied = false;
        reply.setText("");
        reply.setVisible(false);
        clear.setVisible(false);
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
            //TODO decide whether to add this if nonexistent account is handled
            /*int finalI = i;
            sender.setOnMouseClicked(mouseEvent -> client.getPerson(talks[finalI].getSender()));
*/
            Label text = new Label(talks[i].getText());
            text.setId("message");

            messageHBox.setSpacing(25);
            messageHBox.setMinWidth(WIDTH);
            messageHBox.setMaxWidth(WIDTH);

            String name = talks[i].getSender();
            if (colors.get(name) == null) {
                String style = "-fx-text-fill: rgb(" + (int) (Math.random() * 200 + 55) + "," + (int) (Math.random() * 200 + 55) + "," + (int) (Math.random() * 200 + 55) + ");";
                colors.put(name, style);
            }
            System.err.println(colors.get(name));
            sender.setStyle(colors.get(name));


            messageHBox.getChildren().addAll(sender, text);
            text.setOnMouseClicked(mouseEvent -> addReply(text));
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

    private void addReply(Label text) {
        reply.setVisible(true);
        clear.setVisible(true);
        replied = true;
        reply.setText(text.getText());
    }

    public Group getRoot() {
        return root;
    }
}
