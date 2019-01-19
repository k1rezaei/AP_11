import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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
                Game.getInstance().startWorkshop(workshop.getName());
            }
        };
    }

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(Helicopter helicopter) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
                Game.getInstance().well();
            }
        };
    }

}
