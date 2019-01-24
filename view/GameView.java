import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class GameView {
    public static final int BASE_X = 260;
    public static final int BASE_Y = 210;
    private static final int INFO_LENGTH = 20;
    private static final int ONE_SECOND = 1000 * 1000 * 1000;
    private static final GameView gameView = new GameView();
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
    private static final int BUY_ANIMAL_Y = 20;
    private static final int BUY_ANIMAL_BASE_X = 20;
    private static final int BUY_ANIMAL_X_DIFF = 45;
    private static final int GOALS_WIDTH = 70;
    private static final int GOALS_HEIGHT = 50;
    private static final int GOALS_X = 720;
    private static final int GOALS_Y = 550;
    private static final int FF_HEIGHT = 50;
    private static final int FF_WIDTH = 100;
    private static final int FF_X = 450;
    private static final int FF_Y = 15;
    private static final int SAVE_X = 550;
    private static final int SAVE_Y = 15;
    private static final int EXIT_X = 10;
    private static final int EXIT_Y = 550;
    private static final int MONEY_X = 700;
    private static final int MONEY_Y = 20;
    private static final int MENU_X = 110;
    private static final int MENU_Y = 550;
    private static final int MENU_WODTH = 100;
    private static final int MENU_HEIGHT = 50;
    private static final int WAREHOUSE_X = 360;
    private static final int WAREHOUSE_Y = 460;
    private static final int WAREHOUSE_CNT_X = 8;
    private static final int WAREHOUSE_CNT_Y = 4;
    private static final double SOUND_PROP = 0.01;
    private static final String[] NON_WILD = {"chicken", "sheep", "cow", "dog", "cat"};
    private static final double EPS = 0.0001;
    private static final Rectangle REFRESHER = new Rectangle(0, 0, 1000, 1000);
    private static Image info = new Image("file:textures/info.png");

    static {
        REFRESHER.setVisible(false);
    }

    private ArrayList<SpriteAnimation> deadSprites = new ArrayList<>();
    private double speed = 1;
    private boolean paused = false;
    private AnimationTimer game;
    private Label truckInfo;
    private Label helicopterInfo;
    private FlowPane stored = new FlowPane();
    private Group root = new Group();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    private Group entityRoot = new Group();
    private Group infoRoot = new Group();
    private View view;
    private Rectangle filled = new Rectangle(12, 0);
    private HashMap<Workshop, SpriteAnimation> workshops = new HashMap<>();
    private SpriteAnimation well;
    private SpriteAnimation warehouse;
    private SpriteAnimation truck;
    private SpriteAnimation helicopter;
    private Label moneyLabel;
    private Focus focus;

    private GameView() {
    }

    public static GameView getInstance() {
        return gameView;
    }

    public void pause() {
        paused = true;
        game.stop();
    }

    public void resume() {
        paused = false;
        game.start();
    }

    public void setUpDead(Entity entity) {
        if (entity instanceof Animal) Sounds.play(entity.getType() + "_die");
        if ((entity instanceof Animal) && !(entity instanceof WildAnimal)) {
            SpriteAnimation spriteAnimation = Images.getSpriteAnimation(entity.getType());
            spriteAnimation.setState(4);
            fixSprite(spriteAnimation, entity.getDeadCell().getX() + BASE_X - spriteAnimation.getWidth() / 2,
                    entity.getDeadCell().getY() + BASE_Y - spriteAnimation.getHeight() / 2);
            spriteAnimation.setCycleCount(1);
            spriteAnimation.play();
            deadSprites.add(spriteAnimation);
        }
    }

    public void removeFinishedDead() {
        for (int i = deadSprites.size() - 1; i >= 0; i--) {
            if (deadSprites.get(i).getLastIndex() == 23) {
                root.getChildren().remove(deadSprites.get(i).getImageView());
                deadSprites.remove(i);
            }
        }
    }

    public FlowPane getStored() {
        return stored;
    }

    public void runGame() {
        root = new Group();
        entityRoot = new Group();
        infoRoot = new Group();
        workshops.clear();
        sprites.clear();
        deadSprites.clear();
        initializeNodes();

        game = new AnimationTimer() {
            private static final int SECOND = 1000000000;
            int cnt = 0;
            private long lastTime;

            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
                if (now > lastTime + SECOND / (48 * speed)) {
                    infoRoot.toFront();
                    focus.getRoot().toFront();
                    lastTime = now;

                    updateWarehouse();

                    root.getChildren().add(REFRESHER);
                    root.getChildren().remove(REFRESHER);

                    truck.getImageView().setVisible(Game.getInstance().getTruck().getRemainingTime() == 0);
                    truckInfo.setVisible(Game.getInstance().getTruck().getRemainingTime() == 0);

                    helicopter.getImageView().setVisible(Game.getInstance().getHelicopter().getRemainingTime() == 0);
                    helicopterInfo.setVisible(Game.getInstance().getHelicopter().getRemainingTime() == 0);

                    Game.getInstance().turn();
                    for (Entity entity : Game.getInstance().getMap().getEntities()) {
                        if (entity.getCell() != null) {
                            if (entity instanceof Animal) {
                                if (Math.random() < SOUND_PROP) {
                                    Sounds.play(entity.getType() + "_voice");
                                }
                            }
                            if (!sprites.containsKey(entity)) {
                                addSprite(entity);
                            }
                            renderSprite(entity);
                        } else if (sprites.containsKey(entity)) {
                            killSprite(entity);
                        }
                    }
                    removeFinishedDead();
                    for (Workshop workshop : Game.getInstance().getWorkshops()) {
                        if (workshop.getRemainTime() == 0) {
                            getWorkshop(workshop).shutDown();
                        }
                    }
                    Game.getInstance().getMap().relax();
                    filled.setHeight(50 - 50 * (1.0 * Game.getInstance().getWell().getCurrentAmount() / Game.getInstance().getWell().getCapacity()));
                    moneyLabel.setText(Integer.toString(Game.getInstance().getMoney()));
                    if (Game.getInstance().checkLevel()) {
                        pause();
                        AnimationTimer game = this;
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        root.getChildren().clear();
                        ImageView imageView = new ImageView(new Image("file:textures/end3.gif"));
                        imageView.setFitHeight(600);
                        imageView.setFitWidth(800);
                        root.getChildren().add(imageView);
                        Label finish = new Label("You Won The Level :D");
                        finish.translateXProperty().bind(finish.widthProperty().divide(2).negate());
                        finish.translateYProperty().bind(finish.heightProperty().divide(2).negate());
                        finish.setId("finish");
                        finish.relocate(400, 300);
                        root.getChildren().add(finish);
                        AnimationTimer animationTimer = new AnimationTimer() {
                            long last = -1;
                            int cnt = 0;

                            @Override
                            public void handle(long now) {
                                if (last == -1) last = now;
                                if (now - last > ONE_SECOND) {
                                    cnt++;
                                    last = now;
                                    if (finish.getId().equals("finish")) finish.setId("finish2");
                                    else finish.setId("finish");
                                    if (cnt == 5) {
                                        Menu menu = new Menu(view);
                                        view.setRoot(menu.getRoot());
                                        //TODO fix Back to MENU.
                                        game.stop();
                                        this.stop();
                                    }
                                }
                            }
                        };
                        animationTimer.start();
                    }

                }
            }
        };
        resume();
        game.start();
    }

    private void killSprite(Entity entity) {
        setUpDead(entity);
        SpriteAnimation sprite = sprites.get(entity);
        sprite.stop();
        sprite.getImageView().setVisible(false);
        entityRoot.getChildren().remove(sprite.getImageView());
        sprites.remove(entity);
    }

    private void renderSprite(Entity entity) {
        SpriteAnimation sprite = sprites.get(entity);
        if (sprite.getState() != entity.getState()) {
            entityRoot.getChildren().remove(sprite.getImageView());
            sprite.setState(entity.getState());
            entityRoot.getChildren().add(sprite.getImageView());
        }
        sprite.getImageView().setTranslateX(-sprite.getWidth() / 2);
        sprite.getImageView().setTranslateY(-sprite.getHeight() / 2);
        sprite.getImageView().relocate(BASE_X + entity.getCell().getX(), BASE_Y + entity.getCell().getY());
    }

    private void addSprite(Entity entity) {
        SpriteAnimation newSprite = Images.getSpriteAnimation(entity);
        sprites.put(entity, newSprite);
        newSprite.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(entity));
        newSprite.play();
        entityRoot.getChildren().add(newSprite.getImageView());
        if (entity.getType().equalsIgnoreCase("plant")) {
            ImageView plant = newSprite.getImageView();
            plant.toBack();
            plant.setOnMouseClicked(EventHandlers.getOnMouseClickedPlant(
                    entity.getCell().getX() + BASE_X - newSprite.getWidth() / 2,
                    entity.getCell().getY() + BASE_Y - newSprite.getHeight() / 2));
        }
    }

    public Focus getFocus() {
        return focus;
    }

    private void initializeNodes() {
        focus = new Focus();
        setUpBackground();
        setUpBuyIcons();
        setUpMoneyLabel();
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
        root.getChildren().add(entityRoot);
        root.getChildren().add(infoRoot);
        root.getChildren().add(focus.getRoot());
    }

    private void setUpGoals() {
        Level level = Game.getInstance().getLevel();
        Label goals = new Label();
        goals.setId("label_button");
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
        warehouse.setState(Game.getInstance().getWarehouse().getLevel());
        fixSprite(warehouse, WAREHOUSE_X, WAREHOUSE_Y);
        warehouse.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWarehouse()));
        root.getChildren().add(stored);
    }

    private void setUpFastForward() {
        Label ff = new Label();
        ff.setId("label_button");

        ImageView ff1 = new ImageView(new Image("file:textures/fastForward/fastForward1.png"));
        ff1.setFitHeight(FF_HEIGHT);
        ff1.setFitWidth(FF_WIDTH);


        ImageView ff2 = new ImageView(new Image("file:textures/fastForward/fastForward2.png"));
        ff2.setFitHeight(FF_HEIGHT);
        ff2.setFitWidth(FF_WIDTH);

        ff.setGraphic(ff1);
        ff.relocate(FF_X, FF_Y);
        ff.setOnMouseClicked(event -> {
            if (speed < 1 + EPS) {
                speed = 2;
                ff.setGraphic(ff2);
            } else {
                speed = 1;
                ff.setGraphic(ff1);
            }
        });
        root.getChildren().add(ff);
    }

    private void setUpSaveButton() {
        Label save = new Label();
        save.setGraphic(new ImageView(new Image("file:textures/save.png")));
        save.relocate(SAVE_X, SAVE_Y);
        save.setId("label_button");
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
        exit.setId("label_button");
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

        ImageView mn = new ImageView(new Image("file:textures/menu.png"));
        mn.setFitWidth(MENU_WODTH);
        mn.setFitHeight(MENU_HEIGHT);
        Label menu = new Label();
        menu.setId("label_button");
        menu.setGraphic(mn);
        menu.relocate(MENU_X, MENU_Y);

        menu.setOnMouseClicked(event -> {

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Back To Menu");
            pause();

            ButtonType buttonTypeOne = new ButtonType("Save");
            ButtonType buttonTypeTwo = new ButtonType("Do Not Save");
            ButtonType buttonTypeThree = new ButtonType("Cancel");

            // TODO  ButtonType buttonTypeThree = new ButtonType("Go to menu");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

            Optional<ButtonType> result = alert.showAndWait();


            if (result.get() == buttonTypeOne) {
                try {
                    Game.getInstance().saveGame("SaveGame");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (result.get() != buttonTypeThree) {
                root.getChildren().clear();
                Menu backMenu = new Menu(view);
                view.setRoot(backMenu.getRoot());
                game.stop();
                //TODO fix Back to MENU.
            } else resume();

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

            int x, y;

            if (i <= 2) {
                x = LEFT_WORKSHOP_X;
                y = BASE_WORKSHOP + WORKSHOP_DIS * i;
            } else {
                x = RIGHT_WORKSHOP_X;
                y = BASE_WORKSHOP + WORKSHOP_DIS * (i - 3);
            }

            fixSprite(sprite, x, y);

            Label workshopInfo = new Label();
            ImageView img = new ImageView(info);
            img.setFitHeight(INFO_LENGTH);
            img.setFitWidth(INFO_LENGTH);
            workshopInfo.setGraphic(img);


            workshopInfo.relocate(x + 10, y + 10);


            infoRoot.getChildren().add(workshopInfo);

            workshopInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(workshop));
            workshopInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(workshop));

        }
    }

    private void setUpTruck() {
        truck = Images.getSpriteAnimation("truck");
        truck.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getTruck()));
        truck.setState(Game.getInstance().getTruck().getLevel());
        fixSprite(truck, TRUCK_X, TRUCK_Y);

        ImageView img = new ImageView(info);
        img.setFitHeight(INFO_LENGTH);
        img.setFitWidth(INFO_LENGTH);

        truckInfo = new Label();
        truckInfo.setGraphic(img);
        truckInfo.relocate(TRUCK_X - 10, TRUCK_Y);
        truckInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(Game.getInstance().getTruck()));
        truckInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(Game.getInstance().getTruck()));
        infoRoot.getChildren().add(truckInfo);
    }

    private void setUpHelicopter() {
        helicopter = Images.getSpriteAnimation("helicopter");
        helicopter.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getHelicopter()));
        helicopter.setState(Game.getInstance().getHelicopter().getLevel());
        fixSprite(helicopter, HELICOPTER_X, HELICOPTER_Y);

        ImageView img = new ImageView(info);
        img.setFitWidth(INFO_LENGTH);
        img.setFitHeight(INFO_LENGTH);

        helicopterInfo = new Label();
        helicopterInfo.setGraphic(img);
        helicopterInfo.relocate(HELICOPTER_X + 120, HELICOPTER_Y + 100);

        helicopterInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(Game.getInstance().getHelicopter()));
        helicopterInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(Game.getInstance().getHelicopter()));

        infoRoot.getChildren().add(helicopterInfo);
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

        ImageView img = new ImageView(info);
        img.setFitHeight(INFO_LENGTH);
        img.setFitWidth(INFO_LENGTH);

        Label wellInfo = new Label();
        wellInfo.setGraphic(img);
        wellInfo.relocate(WELL_X - 10, WELL_Y + 10);
        infoRoot.getChildren().add(wellInfo);

        wellInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(Game.getInstance().getWell()));
        wellInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(Game.getInstance().getWell()));

    }

    private void setUpBackground() {
        Image backgroundImage = new Image("file:textures/back.png");
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setOnMouseClicked(EventHandlers.getOnMouseClickedPlant(0, 0));
        root.getChildren().add(imageView);
    }

    private void setUpBuyIcons() {
        for (int i = 0; i < NON_WILD.length; i++) {
            String animalName = NON_WILD[i];
            ImageView buyAnimal = Images.getIcon(animalName);
            VBox info = new VBox();
            info.relocate(BUY_ANIMAL_BASE_X + 210, BUY_ANIMAL_Y + 50);
            Label name = new Label(animalName);
            Label price = new Label(Integer.toString(Entity.getNewEntity(animalName).getBuyPrice()));
            info.getChildren().add(name);
            info.getChildren().add(price);
            buyAnimal.setOnMouseClicked(mouseEvent -> {
                try {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        Game.getInstance().buyAnimal(animalName);
                    } else if (mouseEvent.getButton() == MouseButton.SECONDARY && animalName.equalsIgnoreCase("cat")) {
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
                focus.getRoot().getChildren().add(info);
            });
            buyAnimal.setOnMouseExited(mouseEvent -> {
                focus.getRoot().getChildren().remove(info);
            });
            buyAnimal.relocate(BUY_ANIMAL_BASE_X + BUY_ANIMAL_X_DIFF * i, BUY_ANIMAL_Y);
            root.getChildren().add(buyAnimal);
        }
    }

    private void setUpMoneyLabel() {
        moneyLabel = new Label(Integer.toString(Game.getInstance().getMoney()));
        moneyLabel.setTextFill(Color.GOLD);
        moneyLabel.setFont(Font.font(30));
        moneyLabel.relocate(MONEY_X, MONEY_Y);
        root.getChildren().add(moneyLabel);
    }

    private void fixSprite(SpriteAnimation sprite, int x, int y) {
        for (ImageView img : sprite.getImageViews())
            img.relocate(x, y);
        root.getChildren().add(sprite.getImageView());
        sprite.setX(x);
        sprite.setY(y);
    }

    public void updateWarehouse() {
        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();
        stored.getChildren().clear();

        int offsetY = Game.getInstance().getWarehouse().getLevel() * 5;
        stored.relocate(WAREHOUSE_X + 30, offsetY + WAREHOUSE_Y + 40);
        stored.setMinSize(120, 80);
        stored.setMaxSize(120, 80);
        int cur = 0;
        int cnt = 0;
        for (Map.Entry<String, Integer> pair : storables.entrySet()) {
            Entity entity = Entity.getNewEntity(pair.getKey());
            cur += entity.getSize() * pair.getValue();
            while (cur * (WAREHOUSE_CNT_X * WAREHOUSE_CNT_Y) > cnt * (Game.getInstance().getWarehouse().getMaximumCapacity())) {
                cnt++;
                ImageView imageView = Images.getSpriteAnimation(pair.getKey()).getImageView();
                imageView.setFitHeight(80 / WAREHOUSE_CNT_Y);
                imageView.setFitWidth(100 / WAREHOUSE_CNT_X);
                Label label = new Label();
                label.setStyle("-fx-border-color: white;"
                        + "-fx-border-style:dashed;"
                        + "-fx-background-color: black;");
                label.setOpacity(0.5);
                label.setGraphic(imageView);

                label.setMinHeight(80 / WAREHOUSE_CNT_Y);
                label.setMaxHeight(80 / WAREHOUSE_CNT_Y);

                stored.getChildren().add(label);
            }
        }
        stored.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWarehouse()));

    }

    public void update(SpriteAnimation sprite, Upgradable upgradable) {
        root.getChildren().remove(sprite.getImageView());
        sprite.setState(upgradable.getLevel());
        root.getChildren().add(sprite.getImageView());
    }

    public Group getRoot() {
        return root;
    }

    public SpriteAnimation getWorkshop(Workshop workshop) {
        return workshops.get(workshop);
    }

    public SpriteAnimation getWarehouse() {
        return warehouse;
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

    public boolean getPaused() {
        return paused;
    }

    public void setView(View view) {
        this.view = view;

    }
}
