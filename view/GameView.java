import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Optional;


public class GameView {
    private static final GameView gameView = new GameView();
    private static final Game GAME = Game.getInstance();
    private static final int WELL_X = 360;
    private static final int WELL_Y = 20;
    private static final int TRUCK_X = 200;
    private static final int TRUCK_Y = 460;
    private static final int LEFT_WORKSHOP_X = 50;
    private static final int RIGHT_WORKSHOP_X = 620;
    private static final int BASE_WORKSHOP = 80;
    private static final int WORKSHOP_DIS = 150;
    private Group root = new Group();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    private static final int BASE_X = 180;
    private static final int BASE_Y = 130;
    private static final String[] NON_WILD = {"sheep", "chicken", "cow", "dog", "cat"};

    private HashMap<Workshop, SpriteAnimation> workshops = new HashMap<>();

    private SpriteAnimation well;

    private SpriteAnimation truck;

    private GameView() {
    }

    public void initGame() {
        GAME.loadCustom("workshops");
        Images.init();
        runGame("level0"); //TODO create menu and more levels
    }


    public SpriteAnimation getWorkshop(Workshop workshop) {
        return workshops.get(workshop);
    }

    private void fixSprite(SpriteAnimation sprite, int x, int y) {
        for (ImageView img : sprite.getImageViews())
            img.relocate(x, y);
        root.getChildren().add(sprite.getImageView());
    }


    private void runGame(String levelName) {

        GAME.runMap(levelName);
        Image background = new Image("file:textures/back.png");
        ImageView imageView = new ImageView(background);
        root.getChildren().add(imageView);
        for (int i = 0; i < NON_WILD.length; i++) {
            String animalName = NON_WILD[i];
            ImageView buyAnimal = Images.getIcon(animalName);
            buyAnimal.setOnMouseClicked(mouseEvent -> {
                GAME.buyAnimal(animalName);
            });
            buyAnimal.relocate(20 + 45 * i, 20);
            root.getChildren().add(buyAnimal);
        }


        well = Images.getSpriteAnimation("well");
        well.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWell()));
        fixSprite(well, WELL_X, WELL_Y);

        truck = Images.getSpriteAnimation("truck");
        truck.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getTruck()));
        fixSprite(truck, TRUCK_X, TRUCK_Y);


        for (Workshop workshop : Game.getInstance().getWorkshops()) {
            System.err.println(workshop.getName());
            workshops.put(workshop, Images.getSpriteAnimation(workshop.getName()));
        }

        int cnt = 0;
        for (Workshop workshop : Game.getInstance().getWorkshops()) {
            SpriteAnimation sprite = getWorkshop(workshop);
            sprite.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(workshop));
            if (cnt <= 2) fixSprite(sprite, LEFT_WORKSHOP_X, BASE_WORKSHOP + WORKSHOP_DIS * cnt);
            else fixSprite(sprite, RIGHT_WORKSHOP_X, BASE_WORKSHOP + WORKSHOP_DIS * (cnt - 3));
            cnt++;
        }

        Button save = new Button("Save");
        save.relocate(550, 15);
        save.setOnMouseClicked(event -> {
            try {
                Game.getInstance().saveGame("SaveGame");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("^_^");
                alert.setContentText(null);
                alert.setHeaderText("Saved Successful");
                alert.show();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        Button exit = new Button("Exit");
        exit.relocate(10, 550);
        exit.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setContentText("Do You Want To Save Before Exit?");
            //alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try{
                    Game.getInstance().saveGame("SaveGame");
                } catch(Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            //view.close();

        });

        root.getChildren().add(save);
        root.getChildren().add(exit);

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
                    for (Workshop workshop : GAME.getWorkshops()) {
                        if (workshop.getRemainTime() == 0) {
                            getWorkshop(workshop).shutDown();
                        }
                    }
                    GAME.getMap().relax();
                    if (GAME.checkLevel()) {
                        this.stop();
                    }
                }
            }
        };
        game.start();
    }

    public void update(SpriteAnimation sprite, Upgradable upgradable) {
        root.getChildren().remove(sprite.getImageView());
        sprite.setState(upgradable.getLevel());
        root.getChildren().add(sprite.getImageView());
    }

    public static GameView getInstance() {
        return gameView;
    }

    public Group getRoot() {
        return root;
    }

    public SpriteAnimation getWell() {
        return well;
    }

    public SpriteAnimation getTruck() {
        return truck;
    }
}
