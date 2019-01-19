import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class View extends Application {
    Group root = new Group();

    private static final Game GAME = Game.getInstance();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    ArrayList<SpriteAnimation> workshopSprites = new ArrayList<>();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, 800, 600);
        initGame();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        primaryStage.show();
    }

    private void initGame() {
        GAME.loadCustom("workshops");
        //Images.init();
        runGame("level0"); //TODO create menu and more levels
    }

    private void runGame(String levelName) {
        GAME.runMap(levelName);
        Image background = new Image("file:textures/back.png");
        ImageView imageView = new ImageView(background);
        root.getChildren().add(imageView);
        /*AnimationTimer game = new AnimationTimer() {
            private long lastTime;
            private static final int SECOND = 1000000000;

            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
                if (now > lastTime + SECOND / 24) {
                    GAME.turn();
                    for (Entity entity : Game.getInstance().getMap().getEntities()) {
                        if (entity.cell != null) {
                            if (!sprites.containsKey(entity))
                                sprites.put(entity, Images.getSpriteAnimation(entity));
                            SpriteAnimation sprite = sprites.get(entity);
                            root.getChildren().remove(sprite.getImageView());
                            sprite.setState(entity.getState());
                            sprite.getImageView().relocate(entity.getCell().getX(), entity.getCell().getY());
                            root.getChildren().add(sprite.getImageView());
                        } else {
                            if (!sprites.containsKey(entity)) continue;
                            SpriteAnimation sprite = sprites.get(entity);
                            sprite.stop();
                            root.getChildren().remove(sprite.getImageView());
                            sprites.remove(entity);
                        }
                    }

                    if (GAME.checkLevel()) {
                        this.stop();
                    }
                }
            }
        };
        game.start();*/
    }
}
