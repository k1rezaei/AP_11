import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

public class View extends Application {
    private static final boolean INTRO = true;
    /// age ejra nashod true konid
    private boolean mute = true;
    private Stage primaryStage;
    private Scene scene;
    ArrayList<Group> roots = new ArrayList<>();

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
        Images.init();
        if (!mute) Sounds.init();
        Game.loadCustom("workshops");
        GameView.getInstance().setView(this);
        scene.setCursor(new ImageCursor(new Image("file:textures/cursor.png"), 20, 20));
        primaryStage.show();


        if (!mute) {
            Sounds.play("main_theme");
        }

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

    public void goBack(){
        if(roots.size() > 1){
            roots.remove(roots.get(roots.size() - 1));
            scene.setRoot(roots.get(roots.size() - 1));
        }
    }

    public void close() {
        primaryStage.close();
    }

    public Scene getScene(){
        return scene;
    }
}
