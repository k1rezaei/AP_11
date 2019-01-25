import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class EventHandlers {

    static View view;

    //static private Media sound = new Media(new File("sounds/alert.mp3").toURI().toString());
    //static private MediaPlayer mediaPlayer = new MediaPlayer(sound);
    static void setView(View view) {
        EventHandlers.view = view;
    }

    static void alert() {
        //    mediaPlayer.stop();
        //    mediaPlayer.play();
    }

    static EventHandler<MouseEvent> getOnMouseEnteredEventHandler(Warehouse warehouse) {
        return event -> GameView.getInstance().getFocus().add(warehouse);
    }

    static EventHandler<MouseEvent> getOnMouseExitedEventHandler(Warehouse warehouse) {
        return event -> GameView.getInstance().getFocus().remove(warehouse);
    }


    static EventHandler<MouseEvent> getOnMouseEnteredEventHandler(Workshop workshop) {
        return event -> GameView.getInstance().getFocus().add(workshop);
    }

    static EventHandler<MouseEvent> getOnMouseExitedEventHandler(Workshop workshop) {
        return event -> GameView.getInstance().getFocus().remove(workshop);
    }

    static EventHandler<MouseEvent> getOnMouseEnteredEventHandler(Vehicle vehicle) {
        return event -> GameView.getInstance().getFocus().add(vehicle);
    }

    static EventHandler<MouseEvent> getOnMouseExitedEventHandler(Vehicle vehicle) {
        return event -> GameView.getInstance().getFocus().remove(vehicle);
    }

    static EventHandler<MouseEvent> getOnMouseEnteredEventHandler(Well well) {
        return event -> GameView.getInstance().getFocus().add(well);
    }

    static EventHandler<MouseEvent> getOnMouseExitedEventHandler(Well well) {
        return event -> GameView.getInstance().getFocus().remove(well);
    }


    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Entity entity) {
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                try {
                    if (entity instanceof WildAnimal) {
                        Game.getInstance().cage(entity.getCell().getX(), entity.getCell().getY());
                    } else if (entity instanceof Item) {
                        Game.getInstance().pickUp(entity.getCell().getX(), entity.getCell().getY());
                    }
                } catch (Exception e) {
                    if (entity instanceof Item) {
                        alert();
                    }
                    if (e.getMessage() != null) {
                        System.err.println(e.getMessage());
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Workshop workshop) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    try {
                        Game.getInstance().startWorkshop(workshop.getName());
                        GameView.getInstance().getWorkshop(workshop).play();
                    } catch (Exception e) {
                        alert();
                    }
                    break;
                case SECONDARY:
                    try {
                        SpriteAnimation sprite = GameView.getInstance().getWorkshop(workshop);
                        Game.getInstance().upgrade(workshop.getName());
                        GameView.getInstance().update(sprite, workshop);
                    } catch (Exception e) {
                        alert();
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Helicopter helicopter) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    GameView.getInstance().pause();
                    view.setRoot(new BuyMenu(view).getBuyGroup());
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("helicopter");
                        SpriteAnimation sprite = GameView.getInstance().getHelicopter();
                        GameView.getInstance().update(sprite, helicopter);
                    } catch (Exception e) {
                        alert();
                        System.err.println(e.getMessage());
                    }
                    break;
            }

        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Truck truck) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    GameView.getInstance().pause();
                    new SellMenu(view).getSellGroup();
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("truck");
                        SpriteAnimation sprite = GameView.getInstance().getTruck();
                        GameView.getInstance().update(sprite, truck);
                    } catch (Exception e) {
                        alert();
                        System.err.println(e.getMessage());
                    }
                    break;
            }
        };
    }

    static void upgradeWarehouse(Warehouse warehouse) {
        try {
            Game.getInstance().upgrade("warehouse");
            SpriteAnimation sprite = GameView.getInstance().getWarehouse();
            GameView.getInstance().update(sprite, warehouse);

            GameView.getInstance().getRoot().getChildren().remove(GameView.getInstance().getStored());
            GameView.getInstance().getRoot().getChildren().add(GameView.getInstance().getStored());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            alert();
        }
    }


    static EventHandler<MouseEvent> getAltOnMouseClickedEventHandler(Warehouse warehouse) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    break;
                case SECONDARY:
                    upgradeWarehouse(warehouse);
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Warehouse warehouse) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if(Game.getInstance().getTruck().getRemainingTime() == 0) {
                        GameView.getInstance().pause();
                        view.setRoot(new SellMenu(view).getSellGroup());
                    }
                    break;
                case SECONDARY:
                    upgradeWarehouse(warehouse);
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Well well) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    try {
                        Game.getInstance().well();
                        GameView.getInstance().getWell().setCycleCount(1);
                        GameView.getInstance().getWell().play();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        alert();
                    }
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("well");
                        SpriteAnimation sprite = GameView.getInstance().getWell();
                        GameView.getInstance().update(sprite, well);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedPlant(int nodeX, int nodeY) {
        return mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                int x = (int) mouseEvent.getX();
                int y = (int) mouseEvent.getY();
                try {
                    if (new Cell(x + nodeX - GameView.BASE_X, y + nodeY - GameView.BASE_Y).isInside()) {
                        Game.getInstance().addPlant(x + nodeX - GameView.BASE_X, y + nodeY - GameView.BASE_Y);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        };
    }

    public static EventHandler<MouseEvent> getOnMouseClicked(String animalName) {
        return mouseEvent -> {
            try {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    Game.getInstance().buyAnimal(animalName);

                } else if (mouseEvent.getButton() == MouseButton.SECONDARY &&
                        animalName.equalsIgnoreCase("cat")) {
                    Game.getInstance().upgrade("cat");
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    System.err.println(e.getMessage());
                } else {
                    e.printStackTrace();
                }
            }
        };
    }
}
