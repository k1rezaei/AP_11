

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {
    private Stage primaryStage;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        EventHandlers.setView(this);
        this.primaryStage = primaryStage;
        Menu menu = new Menu(this);
        scene = new Scene(menu.getRoot(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        primaryStage.show();
    }
    public void setRoot(Group root) {
        scene.setRoot(root);
    }

    public void close() {
        primaryStage.close();
    }
}
