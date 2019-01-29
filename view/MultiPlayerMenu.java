import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class MultiPlayerMenu {
    private static String BASE = "file:textures/multiplayer/";
    private Group root = new Group();
    private Client client;
    private View view;
    private String id;

    MultiPlayerMenu(View view, Client client) {
        this.view = view;
        this.client = client;
        ImageView imageView = new ImageView(new Image(BASE + "back.jpg"));
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);

        root.getChildren().add(imageView);
        Label chat = new Label("CHAT");
        //chat.setGraphic(new ImageView(new Image(BASE + "chat.png")));
        chat.setId("label_button");

        Label rank = new Label("RANK");
        //rank.setGraphic(new ImageView(new Image(BASE + "rank.png")));
        rank.setId("label_button");

        Label profile = new Label("PROFILE");
        profile.setId("label_button");

        Label logOut = new Label("LOGOUT");
        ImageView temp = new ImageView(new Image(BASE + "logOut.png"));
        temp.setFitWidth(200);
        //logOut.setGraphic(temp);
        logOut.setId("label_button");

        VBox vBox = new VBox(rank, chat, logOut, profile);
        vBox.relocate(400, 300);
        vBox.translateXProperty().bind(vBox.widthProperty().divide(2).negate());
        vBox.translateYProperty().bind(vBox.heightProperty().divide(2).negate());
        root.getChildren().addAll(vBox);

        chat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.err.println(view == null);
                System.err.println(client == null);
                System.err.println(client.getChatroom() == null);
                System.err.println(client.getChatroom().getRoot() == null);
                view.setRoot(client.getChatroom().getRoot());
            }
        });

        rank.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.setRoot(client.getScoreboard().getRoot());
            }
        });

        logOut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.closeSocket();
                view.setRoot(new Menu(view).getRoot());
            }
        });
        profile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.getPerson(client.getMyId());
            }
        });
    }

    public Group getRoot() {
        return root;
    }
}
