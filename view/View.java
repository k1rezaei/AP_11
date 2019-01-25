import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class View extends Application {
    private Stage primaryStage;
    private Scene scene;
    static final boolean INTRO = true;

    public static void main(String[] args) {
        launch(args);
    }




    private AudioClip mainTheme = null;
    //private static Media sound = new Media(new File("file:/sounds/main_theme.mp3").toString())   ;
    //private MediaPlayer mainTheme = new MediaPlayer(sound);


    @Override
    public void start(Stage primaryStage) {
        EventHandlers.setView(this);
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        if(INTRO){
            scene = new Scene(new Intro(this).getRoot(), 800, 600);
        }else {
            Menu menu = new Menu(this);
            scene = new Scene(menu.getRoot(), 800, 600);
        }
        scene.getStylesheets().add("CSS.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        Images.init();
        //Sounds.init();
        Game.loadCustom("workshops");
        GameView.getInstance().setView(this);
        scene.setCursor(new ImageCursor(new Image("file:textures/cursor.png"), 20, 20));
        primaryStage.show();


        try {
            mainTheme = new AudioClip(new File("sounds/main_theme.mp3").toURI().toURL().toString());
        }catch (Exception e){
        }

        mainTheme.setCycleCount(AudioClip.INDEFINITE);
        mainTheme.play();

    }

    public Image getSnap(){
        return  scene.snapshot(null);
    }

    public void setRoot(Group root) {
        scene.setRoot(root);
    }

    public void close() {
        primaryStage.close();
    }
}
