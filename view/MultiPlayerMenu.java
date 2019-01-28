import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;



public class MultiPlayerMenu {
    private static String BASE = "file:textures/multiplayer";
    private Group root = new Group();
    private Client client;
    private View view;

    MultiPlayerMenu (View view,Client client){
        this.view = view;
        this.client = client;
        ImageView imageView = new ImageView(new Image(BASE + "back.jpg"));

        root.getChildren().add(imageView);
        Label chat = new Label("chat");
        chat.setId("label_button");

        Label rank = new Label("rank");
        rank.setId("label_button");
        VBox vBox = new VBox(rank,chat);
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
    }
    public Group getRoot() {
        return root;
    }
}
