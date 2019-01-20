import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameView {
    private GameView gameView = new GameView();
    private static final Game GAME = Game.getInstance();
    private Group root = new Group();
    private ArrayList<SpriteAnimation> workshopSprites = new ArrayList<>();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    private static final int BASE_X = 180;
    private static final int BASE_Y = 130;

    private GameView() {
    }

    private void initGame() {
        GAME.loadCustom("workshops");
        Images.init();
        runGame("level0"); //TODO create menu and more levels
    }

    private void runGame(String levelName) {
        GAME.runMap(levelName);
        Image background = new Image("file:textures/back.png");
        ImageView imageView = new ImageView(background);
        root.getChildren().add(imageView);

        AnimationTimer game = new AnimationTimer() {
            private static final int SECOND = 1000000000;
            private long lastTime;

            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
                if (now > lastTime + SECOND / 48) {
                    lastTime = now;
                    GAME.turn();
                    for (Entity entity : Game.getInstance().getMap().getEntities()) {
                        if (entity.getCell() != null) {
                            if (!sprites.containsKey(entity)) {
                                SpriteAnimation newSprite = Images.getSpriteAnimation(entity);
                                sprites.put(entity, newSprite);
                                newSprite.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(entity));
                                newSprite.play();
                                root.getChildren().add(newSprite.getImageView());
                            }
                            SpriteAnimation sprite = sprites.get(entity);
                            if (sprite.getState() != entity.getState()) {
                                root.getChildren().remove(sprite.getImageView());
                                sprite.setState(entity.getState());
                                root.getChildren().add(sprite.getImageView());
                            }
                            sprite.getImageView().relocate(BASE_X + entity.getCell().getX(), BASE_Y + entity.getCell().getY());
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

    public GameView getInstance() {
        return gameView;
    }


/**
    void showWareHouse(){
        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();
        for (Map.Entry<String, Integer> pair : storables.entrySet()){

        }

    }*/
}
