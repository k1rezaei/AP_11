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
    private View view;

    MultiPlayerMenu (View view){
        this.view = view;
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
                System.err.println(Game.getInstance().getClient() == null);
                System.err.println(Game.getInstance().getClient().getChatroom() == null);
                System.err.println(Game.getInstance().getClient().getChatroom().getRoot() == null);
                view.setRoot(Game.getInstance().getClient().getChatroom().getRoot());
            }
        });

        rank.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.setRoot(Game.getInstance().getClient().getScoreboard().getRoot());
            }
        });
    }
    public Group getRoot() {
        return root;
    }
}
