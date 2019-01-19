import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static javafx.scene.input.MouseButton.*;

public class EventHandlers {

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(Entity entity) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (entity instanceof WildAnimal) {
                    Game.getInstance().cage(entity.getCell().getX(), entity.getCell().getY());
                }else if(entity instanceof Item){
                    Game.getInstance().pickUp(entity.getCell().getX(), entity.getCell().getY());
                }
            }
        };
    }

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(Workshop workshop) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()){
                    case PRIMARY:
                        Game.getInstance().startWorkshop(workshop.getName());
                        break;
                    case SECONDARY:
                        Game.getInstance().upgrade(workshop.getName());
                        break;
                }
            }
        };
    }

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(Helicopter helicopter) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        break;
                    case SECONDARY:
                        break;
                }
                /**
                 *
                 *
                 * */
            }
        };
    }

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(Truck truck) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        break;
                    case SECONDARY:
                        break;
                }
                /**
                 *
                 *
                 * */
            }
        };
    }

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(Warehouse warehouse) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        break;
                    case SECONDARY:
                        break;
                }
                /**
                 *
                 *
                 * */
            }
        };
    }

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(Well well) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        Game.getInstance().well();
                        break;
                    case SECONDARY:
                        Game.getInstance().upgrade("well");
                        break;
                }
            }
        };
    }

}
