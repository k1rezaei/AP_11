import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class View extends Application {

    HashMap<Entity,SpriteAnimation> sprites = new HashMap<>();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root,700,700);
        initGame();
        runGame();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Friendzy");
        primaryStage.show();
    }

    private void initGame() {
    }

    private void runGame(){
        AnimationTimer game = new AnimationTimer() {
            private long lastTime;
            private static final int SECOND = 1000000000;
            @Override
            public void handle(long now) {
                if(lastTime == 0) lastTime = now;
                if(now > lastTime + SECOND / 30){
                    Game.getInstance().turn();
                    for(Entity entity:Game.getInstance().getMap().getEntities()){
                        if(entity.cell!=null){
                            if(!sprites.containsKey(entity)) sprites.put(entity, Images.getSpriteAnimation(entity));
                            SpriteAnimation sprite = sprites.get(entity);
                            sprite.setState(entity.getState());
                            sprite.getImageView().relocate(entity.getCell().getX(),entity.getCell().getY());
                        }
                        else{
                            if(!sprites.containsKey(entity)) continue;
                            sprites.get(entity).stop();
                            sprites.remove(entity);
                        }
                    }
                }
            }
        };
        game.start();
    }
}
