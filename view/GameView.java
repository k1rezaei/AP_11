import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class GameView {
    private static final GameView gameView = new GameView();
    //private static final Game GAME = Game.getInstance();
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
    public static final int GOALS_WIDTH = 70;
    public static final int GOALS_HEIGHT = 50;
    public static final int GOALS_X = 720;
    public static final int GOALS_Y = 550;
    public static final int FF_HEIGHT = 50;
    public static final int FF_WIDTH = 100;
    public static final int FF_X = 450;
    public static final int FF_Y = 15;
    public static final int SAVE_X = 550;
    public static final int SAVE_Y = 15;
    public static final int EXIT_X = 10;
    public static final int EXIT_Y = 550;
    public static final int MONEY_X = 700;
    public static final int MONEY_Y = 20;
    public static final int WAREHOUSE_X = 360;
    public static final int WAREHOUSE_Y = 460;
    public static final int WAREHOUSE_CNT_X = 8;
    public static final int WAREHOUSE_CNT_Y = 4;
    public static final double SOUND_PROP = 0.01;
    private Group root = new Group();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    private static final int BASE_X = 180;
    private static final int  BASE_Y = 130;
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

    public boolean getPaused() {
        return paused;
    }

    private SpriteAnimation well;
    private SpriteAnimation warehouse;
    private SpriteAnimation truck;
    private SpriteAnimation helicopter;

    public SpriteAnimation getWarehouse() {
        return warehouse;
    }

    public boolean getPause() {
        return paused;
    }

    public void pause() {
        paused = true;
        game.stop();
    }

    public void resume() {
        paused = false;
        game.start();
    }

    private GameView() {
    }

    public void initGame(Level level) {
        Game.getInstance().loadCustom("workshops");
        Images.init();
        Sounds.init();
        runGame(level);
    }

    public boolean initGame() {
        Game.getInstance().loadCustom("workshops");
        Images.init();
        Sounds.init();

        List<String> choices = new ArrayList<>();
        choices.add("level0");
        choices.add("level1");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("level0", choices);
        dialog.setTitle("Choose Level");
        dialog.setHeaderText(null);
        dialog.setContentText(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            runGame(Game.getInstance().getLevel(result.get()));
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

    FlowPane stored = new FlowPane();

    public void updateWarehouse() {
        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();
        root.getChildren().remove(stored);
        stored.getChildren().clear();

        int offset_y = Game.getInstance().getWarehouse().getLevel() * 5;
        stored.relocate(WAREHOUSE_X + 30, offset_y + WAREHOUSE_Y + 40);
        stored.setMaxHeight(80);
        stored.setMaxWidth(120);
        int cur = 0;
        int cnt = 0;
        for (Map.Entry<String, Integer> pair : storables.entrySet()) {
            Entity entity = Entity.getNewEntity(pair.getKey());
            cur += entity.getSize() * pair.getValue();
            while (cur * (WAREHOUSE_CNT_X * WAREHOUSE_CNT_Y) > cnt * (Game.getInstance().getWarehouse().getMaximumCapacity())) {
                cnt++;
                ImageView imageView = Images.getSpriteAnimation(pair.getKey()).getImageView();
                imageView.setFitHeight(70 / WAREHOUSE_CNT_Y);
                imageView.setFitWidth(100 / WAREHOUSE_CNT_X);
                Label label = new Label();
                label.setStyle("-fx-border-color: white;"
                        + "-fx-border-style:dashed;"
                        + "-fx-background-color: black;");
                label.setOpacity(0.5);
                label.setGraphic(imageView);
                label.setMaxHeight(70 / WAREHOUSE_CNT_Y);

                stored.getChildren().add(label);
            }
        }
        root.getChildren().add(stored);
    }




    private void runGame(Level level) {

        Game.getInstance().runMap(level);

        setUpBackground();
        setUpBuyIcons();
        Label moneyLabel = setUpMoneyLabel();
        setUpWell();
        setUpTruck();
        setUpWarehouse();
        setUpWorkshops();
        setUpHelicopter();
        setUpSaveButton();
        setUpFastForward();
        setUpExitButton();
        setUpGoals();
        setUpMenuButton();

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

                    updateWarehouse();

                    if (Game.getInstance().getTruck().getRemainingTime() != 0) {
                        truck.getImageView().setVisible(false);
                    } else truck.getImageView().setVisible(true);

                    if (Game.getInstance().getHelicopter().getRemainingTime() != 0) {
                        helicopter.getImageView().setVisible(false);
                    } else helicopter.getImageView().setVisible(true);


                    lastTime = now;
                    Game.getInstance().turn();
                    for (Entity entity : Game.getInstance().getMap().getEntities()) {
                        if (entity.getCell() != null) {
                            if(entity instanceof Animal){
                                if(Math.random() < SOUND_PROP){
                                    Sounds.play(entity.getType()+"_voice");
                                }
                            }
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
                            if (entity instanceof Animal) Sounds.play(entity.getType() + "_die");
                            SpriteAnimation sprite = sprites.get(entity);
                            sprite.stop();
                            sprite.getImageView().setVisible(false);
                            root.getChildren().remove(sprite.getImageView());
                            sprites.remove(entity);
                        }
                    }
                    for (Workshop workshop : Game.getInstance().getWorkshops()) {
                        if (workshop.getRemainTime() == 0) {
                            getWorkshop(workshop).shutDown();
                        }
                    }
                    Game.getInstance().getMap().relax();
                    filled.setHeight(50 - 50 * (1.0 * Game.getInstance().getWell().getCurrentAmount() / Game.getInstance().getWell().getCapacity()));
                    moneyLabel.setText(Integer.toString(Game.getInstance().getMoney()));
                    if (Game.getInstance().checkLevel()) {
                        this.stop();
                        //TODO go back to menu
                    }
                }
            }
        };
        game.start();
    }

    private void setUpGoals() {
        Level level = Game.getInstance().getLevel();
        Label goals = new Label();
        ImageView goal = new ImageView(new Image("file:textures/goals.png"));
        goal.setFitWidth(GOALS_WIDTH);
        goal.setFitHeight(GOALS_HEIGHT);
        goals.setGraphic(goal);
        goals.relocate(GOALS_X, GOALS_Y);
        root.getChildren().add(goals);

        goals.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Goals");
                alert.setContentText(level.toString());
                alert.setHeaderText(null);
                alert.show();
            }
        });
    }

    private void setUpWarehouse() {
        warehouse = Images.getSpriteAnimation("warehouse");
        warehouse.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWarehouse()));
        warehouse.setState(Game.getInstance().getWarehouse().getLevel());
        fixSprite(warehouse, WAREHOUSE_X, WAREHOUSE_Y);
        root.getChildren().add(stored);
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
        ff1.setFitHeight(FF_HEIGHT);
        ff1.setFitWidth(FF_WIDTH);


        ImageView ff2 = new ImageView(new Image("file:textures/fastForward/fastForward2.png"));
        ff2.setFitHeight(FF_HEIGHT);
        ff2.setFitWidth(FF_WIDTH);

        ff.setGraphic(ff1);
        ff.relocate(FF_X, FF_Y);
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
        save.relocate(SAVE_X, SAVE_Y);
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
        exit.relocate(EXIT_X, EXIT_Y);
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
            } else {
                resume();
            }


        });
        root.getChildren().add(exit);
    }

    private void setUpMenuButton() {

        Label menu = new Label();
        menu.setGraphic(new ImageView(new Image("file:textures/exit.png")));
        menu.relocate(70, 550);

        menu.setOnMouseClicked(event -> {

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Back To Menu");
            pause();

            ButtonType buttonTypeOne = new ButtonType("Save");
            ButtonType buttonTypeTwo = new ButtonType("Do Not Save");
            // TODO  ButtonType buttonTypeThree = new ButtonType("Go to menu");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                try {
                    Game.getInstance().saveGame("SaveGame");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            root.getChildren().clear();
            Menu backMenu = new Menu(view);
            view.setRoot(backMenu.getRoot());

        });
        root.getChildren().add(menu);
    }

    private void setUpWorkshops() {
        workshops.clear();
        for (int i = 0; i < Game.getInstance().getWorkshops().size(); i++) {
            Workshop workshop = Game.getInstance().getWorkshops().get(i);
            workshops.put(workshop, Images.getSpriteAnimation(workshop.getName()));
            SpriteAnimation sprite = getWorkshop(workshop);
            sprite.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(workshop));
            sprite.setState(workshop.getLevel());
            System.out.println("KIRE KHAR");
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
                        Game.getInstance().addPlant(x - BASE_X - 20, y - BASE_Y - 20);
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
                        Game.getInstance().buyAnimal(animalName);
                    else if (mouseEvent.getButton() == MouseButton.SECONDARY && animalName.equalsIgnoreCase("cat")) {
                        Game.getInstance().upgrade("cat");
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
        Label moneyLabel = new Label(Integer.toString(Game.getInstance().getMoney()));
        moneyLabel.setTextFill(Color.GOLD);
        moneyLabel.setFont(Font.font(30));
        moneyLabel.relocate(MONEY_X, MONEY_Y);
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
