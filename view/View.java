import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root,700,700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        primaryStage.show();
    }
}
