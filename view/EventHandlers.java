import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class EventHandlers {

    static View view;

    static void setView(View view) {
        EventHandlers.view = view;
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Entity entity) {
        return event -> {
            if (entity instanceof WildAnimal) {
                Game.getInstance().cage(entity.getCell().getX(), entity.getCell().getY());
            } else if (entity instanceof Item) {
                Game.getInstance().pickUp(entity.getCell().getX(), entity.getCell().getY());
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
                    }
                    break;
                case SECONDARY:
                    try {
                        SpriteAnimation sprite = GameView.getInstance().getWorkshop(workshop);
                        Game.getInstance().upgrade(workshop.getName());

                        GameView.getInstance().update(sprite, workshop);
                    } catch (Exception e) {
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
                    /**
                     *
                     * TODO
                     *
                     * */
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("helicopter");
                        ///TODO         GameView.getInstance().getHelicopter().setState(helicopter.getLevel());
                    } catch (Exception e) {
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
                    view.setRoot(new BuyMenu(view).getBuyGroup());
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("truck");
                        GameView.getInstance().getTruck().setState(truck.getLevel());
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Warehouse warehouse) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        view.setRoot(new BuyMenu(view).getBuyGroup());
                        break;
                    case SECONDARY:
                        try {
                            Game.getInstance().upgrade("warehouse");
                            /// TODO      GameView.getInstance().getWareHouse().setState(warehouse.getLevel());
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                }
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Well well) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        try {
                            Game.getInstance().well();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                        break;
                    case SECONDARY:
                        try {
                            Game.getInstance().upgrade("well");
                            SpriteAnimation sprite = GameView.getInstance().getWell();
                            GameView.getInstance().update(sprite, well);
                            /*GameView.getInstance().getRoot().getChildren().remove(sprite.getImageView());
                            sprite.setState(well.getLevel());
                            GameView.getInstance().getRoot().getChildren().add(sprite.getImageView());
                            */
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                }
            }
        };
    }

}
