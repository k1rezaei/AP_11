import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.stage.Stage;

public class View extends Application {
    private Stage primaryStage;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    //private static Media sound = new Media("file://sounds/main_theme.mp3");
    //private MediaPlayer mediaPlayer = new MediaPlayer(sound);


    @Override
    public void start(Stage primaryStage) {
        EventHandlers.setView(this);
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        Menu menu = new Menu(this);
        scene = new Scene(menu.getRoot(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        Images.init();
        //Sounds.init();
        Game.loadCustom("workshops");
        GameView.getInstance().setView(this);
        scene.setCursor(new ImageCursor(new Image("file:textures/cursor.png"), 20, 20));
        primaryStage.show();

        // mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        // mediaPlayer.play();
    }

    public void setRoot(Group root) {
        scene.setRoot(root);
    }

    public void close() {
        primaryStage.close();
    }
}
