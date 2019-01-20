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
    private static final Game GAME = Game.getInstance();
    Group root = new Group();
    ArrayList<SpriteAnimation> workshopSprites = new ArrayList<>();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    private Stage primaryStage;

    private SpriteAnimation well;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Scene scene = new Scene(root, 800, 600);
        initGame();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        primaryStage.show();
    }

    private void initGame() {
        GAME.loadCustom("workshops");
        Images.init();
        runGame("level0"); //TODO create menu and more levels
    }

    public SpriteAnimation getWell() {
        return well;
    }

    private void runGame(String levelName) {
        GAME.runMap(levelName);
        Image background = new Image("file:textures/back.png");
        ImageView imageView = new ImageView(background);
        root.getChildren().add(imageView);

        well = Images.getSpriteAnimation("well");

        AnimationTimer game = new AnimationTimer() {
            private static final int SECOND = 100000000;
            private long lastTime;

            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
                if (now > lastTime + SECOND/3) {
                    lastTime = now;
                    GAME.turn();
                    for (Entity entity : Game.getInstance().getMap().getEntities()) {
                        if (entity.cell != null) {
                            if (!sprites.containsKey(entity)) {
                                SpriteAnimation newSprite = Images.getSpriteAnimation(entity);
                                sprites.put(entity, newSprite);
                                newSprite.play();
                                root.getChildren().add(newSprite.getImageView());
                            }
                            SpriteAnimation sprite = sprites.get(entity);
                            if (sprite.getState() != entity.getState()) {
                                root.getChildren().remove(sprite.getImageView());
                                sprite.setState(entity.getState());
                                root.getChildren().add(sprite.getImageView());
                            }
                            sprite.getImageView().relocate(entity.getCell().getX(), entity.getCell().getY());
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
        game.start();
    }

    public void close() {
        primaryStage.close();
    }
}
