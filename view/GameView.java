import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class GameView {
    private static final GameView gameView = new GameView();
    private static final Game GAME = Game.getInstance();
    private static final int WELL_X = 360;
    private static final int WELL_Y = 20;
    private static final int TRUCK_X = 200;
    private static final int TRUCK_Y = 460;
    private static final int HELICOPTER_X = 520;
    private static final int HELICOPTER_Y = 430;
    private static final int LEFT_WORKSHOP_X = 50;
    private static final int RIGHT_WORKSHOP_X = 620;
    private static final int BASE_WORKSHOP = 80;
    private static final int WORKSHOP_DIS = 150;
    public static final int BUY_ANIMAL_Y = 20;
    public static final int BUY_ANIMAL_BASE_X = 20;
    public static final int BUY_ANIMAL_X_DIFF = 45;
    private Group root = new Group();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    private static final int BASE_X = 180;
    private static final int BASE_Y = 130;
    private static final String[] NON_WILD = {"chicken", "sheep", "cow", "dog", "cat"};
    private static double SPEED = 1;
    private static final double EPS = 0.0001;
    private static boolean paused = false;
    private static AnimationTimer game;
    private View view;
    Rectangle filled = new Rectangle(12, 0);
    private HashMap<Workshop, SpriteAnimation> workshops = new HashMap<>();
    private static final Rectangle REFRESHER = new Rectangle(0, 0, 1000, 1000);

    static {
        REFRESHER.setVisible(false);
    }

    public boolean getPaused(){return paused;}

    private SpriteAnimation well;

    private SpriteAnimation truck;
    private SpriteAnimation helicopter;

    public boolean getPause() {
        return paused;
    }

    public void pause() {
        paused = true;
        game.stop();
    }
    public void resume(){
        paused = false;
        game.start();
    }

    private GameView() {
    }

    public void initGame(Level level) {
        GAME.loadCustom("workshops");
        Images.init();
        runGame(level);
    }

    public boolean initGame() {
        GAME.loadCustom("workshops");
        Images.init();

        List<String> choices = new ArrayList<>();
        choices.add("level0");
        choices.add("level1");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("level0", choices);
        dialog.setTitle("Choose Level");
        dialog.setHeaderText(null);
        dialog.setContentText(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            runGame(GAME.getLevel(result.get()));
        } else {
            return false;
        }
        return true;

    }


    public SpriteAnimation getWorkshop(Workshop workshop) {
        return workshops.get(workshop);
    }

    private void fixSprite(SpriteAnimation sprite, int x, int y) {
        for (ImageView img : sprite.getImageViews())
            img.relocate(x, y);
        root.getChildren().add(sprite.getImageView());
    }


    private void runGame(Level level) {

        GAME.runMap(level);

        setUpBackground();
        setUpBuyIcons();
        Label moneyLabel = setUpMoneyLabel();
        setUpWell();
        setUpTruck();
        setUpWorkshops();
        setUpHelicopter();
        setUpSaveButton();
        setUpFastForward();
        setUpExitButton();

        game = new AnimationTimer() {
            private static final int SECOND = 1000000000;
            private long lastTime;

            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
                if (now > lastTime + SECOND / (48 * SPEED)) {
                    //TODO ye chiz behtar az 2 khat payin bezanim
                    root.getChildren().add(REFRESHER);
                    root.getChildren().remove(REFRESHER);

                    if(Game.getInstance().getTruck().getRemainingTime() != 0){
                        truck.getImageView().setVisible(false);
                    }else truck.getImageView().setVisible(true);

                    if(Game.getInstance().getHelicopter().getRemainingTime() != 0){
                        helicopter.getImageView().setVisible(false);
                    }else helicopter.getImageView().setVisible(true);


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
                            sprite.getImageView().setVisible(false);
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
                    filled.setHeight(50 - 50 * (1.0 * GAME.getWell().getCurrentAmount() / GAME.getWell().getCapacity()));
                    moneyLabel.setText(Integer.toString(GAME.getMoney()));
                    if (GAME.checkLevel()) {
                        this.stop();
                        //TODO go back to menu
                    }
                }
            }
        };
        game.start();
    }

    private void setUpHelicopter() {
        helicopter = Images.getSpriteAnimation("helicopter");
        helicopter.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getHelicopter()));
        helicopter.setState(Game.getInstance().getHelicopter().getLevel());
        fixSprite(helicopter, HELICOPTER_X, HELICOPTER_Y);
    }

    private void setUpFastForward() {
        Label ff = new Label();

        ImageView ff1 = new ImageView(new Image("file:textures/fastForward/fastForward1.png"));
        ff1.setFitHeight(50);
        ff1.setFitWidth(100);


        ImageView ff2 = new ImageView(new Image("file:textures/fastForward/fastForward2.png"));
        ff2.setFitHeight(50);
        ff2.setFitWidth(100);

        ff.setGraphic(ff1);
        ff.relocate(450, 15);
        ff.setOnMouseClicked(event -> {
            if (SPEED < 1 + EPS) {
                SPEED = 2;
                ff.setGraphic(ff2);
            } else {
                SPEED = 1;
                ff.setGraphic(ff1);
            }
        });
        root.getChildren().add(ff);
    }

    private void setUpSaveButton() {
        Label save = new Label();
        save.setGraphic(new ImageView(new Image("file:textures/save.png")));
        save.relocate(550, 15);
        save.setOnMouseClicked(event -> {
            try {
                Game.getInstance().saveGame("SaveGame");
                pause();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("^_^");
                alert.setContentText(null);
                alert.setHeaderText("Saved Successful");
                alert.showAndWait();
                resume();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        root.getChildren().add(save);
    }

    private void setUpExitButton() {


        Label exit = new Label();
        exit.setGraphic(new ImageView(new Image("file:textures/exit.png")));
        exit.relocate(10, 550);
        exit.setOnMouseClicked(event -> {

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Exit");
            pause();

            ButtonType buttonTypeOne = new ButtonType("Save & Exit");
            ButtonType buttonTypeTwo = new ButtonType("Exit");
            // TODO  ButtonType buttonTypeThree = new ButtonType("Go to menu");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                try {
                    Game.getInstance().saveGame("SaveGame");
                } catch (Exception e) {

                }
                view.close();
            } else if (result.get() == buttonTypeTwo) {
                view.close();
            }else{
                resume();
            }


        });
        root.getChildren().add(exit);
    }

    private void setUpWorkshops() {
        for (int i = 0; i < GAME.getWorkshops().size(); i++) {
            Workshop workshop = GAME.getWorkshops().get(i);
            workshops.put(workshop, Images.getSpriteAnimation(workshop.getName()));
            SpriteAnimation sprite = getWorkshop(workshop);
            sprite.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(workshop));
            sprite.setState(workshop.getLevel());
            if (i <= 2) fixSprite(sprite, LEFT_WORKSHOP_X, BASE_WORKSHOP + WORKSHOP_DIS * i);
            else fixSprite(sprite, RIGHT_WORKSHOP_X, BASE_WORKSHOP + WORKSHOP_DIS * (i - 3));
        }
    }

    private void setUpTruck() {
        truck = Images.getSpriteAnimation("truck");
        truck.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getTruck()));
        truck.setState(Game.getInstance().getTruck().getLevel());
        fixSprite(truck, TRUCK_X, TRUCK_Y);
    }

    private void setUpWell() {
        well = Images.getSpriteAnimation("well");
        well.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWell()));
        well.setState(Game.getInstance().getWell().getLevel());
        fixSprite(well, WELL_X, WELL_Y);
        Rectangle waterBar = new Rectangle(12, 50);
        waterBar.setFill(Color.BLUE);
        waterBar.relocate(WELL_X, WELL_Y + 65);
        root.getChildren().add(waterBar);
        filled.setFill(Color.WHITE);
        filled.relocate(WELL_X, WELL_Y + 65);
        root.getChildren().add(filled);
    }

    private void setUpBackground() {
        Image background = new Image("file:textures/back.png");
        ImageView imageView = new ImageView(background);
        imageView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                int x = (int) mouseEvent.getX();
                int y = (int) mouseEvent.getY();
                try {
                    if (new Cell(x - BASE_X - 20, y - BASE_Y - 20).isInside()) {
                        GAME.addPlant(x - BASE_X - 20, y - BASE_Y - 20);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        });
        root.getChildren().add(imageView);
    }

    private void setUpBuyIcons() {
        for (int i = 0; i < NON_WILD.length; i++) {
            String animalName = NON_WILD[i];
            ImageView buyAnimal = Images.getIcon(animalName);
            buyAnimal.setOnMouseClicked(mouseEvent -> {
                try {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY)
                        GAME.buyAnimal(animalName);
                    else if (mouseEvent.getButton() == MouseButton.SECONDARY && animalName.equalsIgnoreCase("cat")) {
                        GAME.upgrade("cat");
                    }
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        System.err.println(e.getMessage());
                    } else {
                        e.printStackTrace();
                    }
                }
            });

            buyAnimal.setOnMouseEntered(mouseEvent -> {
            });
            buyAnimal.relocate(BUY_ANIMAL_BASE_X + BUY_ANIMAL_X_DIFF * i, BUY_ANIMAL_Y);
            root.getChildren().add(buyAnimal);
        }
    }

    private Label setUpMoneyLabel() {
        Label moneyLabel = new Label(Integer.toString(GAME.getMoney()));
        moneyLabel.setTextFill(Color.GOLD);
        moneyLabel.setFont(Font.font(30));
        moneyLabel.relocate(700, 20);
        root.getChildren().add(moneyLabel);
        return moneyLabel;
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

    public SpriteAnimation getHelicopter() {
        return helicopter;
    }

    public void setView(View view) {
        this.view = view;

    }
}
