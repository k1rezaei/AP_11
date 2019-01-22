

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class View extends Application {
    private Stage primaryStage;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    private static Media sound = new Media(new File("sounds/main_theme.mp3").toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);


    @Override
    public void start(Stage primaryStage) {
        EventHandlers.setView(this);
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        Menu menu = new Menu(this);
        scene = new Scene(menu.getRoot(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        scene.setCursor(new ImageCursor(new Image("file:textures/cursor.png"),20,20));

        primaryStage.show();


        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void setRoot(Group root) {
        scene.setRoot(root);
    }

    public void close() {
        primaryStage.close();
    }
}
