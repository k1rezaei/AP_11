import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class View extends Application {
    private static final boolean INTRO = true;
    private ArrayList<Group> roots = new ArrayList<>();
    /// age ejra nashod true konid
    private boolean mute = true;
    private Stage primaryStage;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        EventHandlers.setView(this);
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        scene = new Scene(new Group(), 800, 600);
        if (INTRO) {
            setRoot(new Intro(this).getRoot());
        } else {
            setRoot(new Menu(this).getRoot());
        }
        scene.getStylesheets().add("CSS.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        primaryStage.getIcons().add(new Image("file:textures/icon.png"));
        Images.init();
        if (!mute) Sounds.init();
        Game.loadCustom("workshops");
        GameView.getInstance().setView(this);
        scene.setCursor(new ImageCursor(new Image("file:textures/cursor.png"), 20, 20));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                close();
            }
        });


        if (!mute) {

            Sounds.get("main_theme").setCycleCount(MediaPlayer.INDEFINITE);
            Sounds.get("main_theme").play();
        }

        cheat();
    }

    void cheat() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.F1) {
                    Game.getInstance().setMoney(10 * 1000 * 1000 - 1);
                }
            }
        });
    }

    boolean getMute() {
        return mute;
    }

    void setMute(boolean mute) {
        this.mute = mute;
    }

    public Image getSnap() {
        return scene.snapshot(null);
    }

    public void setRoot(Group root) {
        scene.setRoot(root);
        roots.add(root);
    }

    public void goBack() {
        if (roots.size() > 1) {
            roots.remove(roots.size() - 1);
            scene.setRoot(roots.get(roots.size() - 1));
        }
    }

    public void close() {
        primaryStage.close();
        System.exit(0);
    }

    public Scene getScene() {
        return scene;
    }
}
