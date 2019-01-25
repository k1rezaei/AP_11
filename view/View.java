import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;

public class View extends Application {
    private static final boolean INTRO = true;
    /// age ejra nashod true konid
    public static boolean MUTE = false;
    private Stage primaryStage;
    private Scene scene;
    private Media music;
    private MediaPlayer mainTheme;

    {
        if (!MUTE) {
            music = new Media(new File("sounds/main_theme.mp3").toURI().toString());
            mainTheme = new MediaPlayer(music);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        EventHandlers.setView(this);
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        if (INTRO) {
            scene = new Scene(new Intro(this).getRoot(), 800, 600);
        } else {
            Menu menu = new Menu(this);
            scene = new Scene(menu.getRoot(), 800, 600);
        }
        scene.getStylesheets().add("CSS.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        Images.init();
        if (!MUTE) Sounds.init();
        Game.loadCustom("workshops");
        GameView.getInstance().setView(this);
        scene.setCursor(new ImageCursor(new Image("file:textures/cursor.png"), 20, 20));
        primaryStage.show();


        if (!MUTE) {
            mainTheme.setCycleCount(AudioClip.INDEFINITE);
            mainTheme.play();
        }

    }

    public Image getSnap() { return scene.snapshot(null); }

    public void setRoot(Group root) {
        scene.setRoot(root);
    }

    public void close() {
        primaryStage.close();
    }
}
