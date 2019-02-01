import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class MultiPlayerMenu {
    private static String BASE = "file:textures/multiplayer/";
    private Group root = new Group();
    private Client client;
    private View view;
    private String id;
    private String[] itemList = new String[]{"Adornment", "CheeseFerment", "Cookie", "Souvenir",
            "bear", "Cheese", "Horn",
            "BrightHorn", "ColoredPlume", "Intermediate",
            "Curd", "lion",
            "Egg", "Milk",
            "EggPowder", "Plume",
            "Cake", "Fabric", "Sewing", "Varnish",
            "CarnivalDress", "Flour", "SourCream", "Wool"};

    MultiPlayerMenu(View view, Client client, boolean isHost) {
        this.view = view;
        this.client = client;
        ImageView imageView = new ImageView(new Image(BASE + "back.jpg"));
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);

        root.getChildren().add(imageView);

        Label start = setUpStart();
        Label chat = setUpChat();
        Label rank = setUpRank();
        Label profile = setUpProfile();
        Label logOut = setUpLogOut();
        Label setPrice = setUpChangePrices();

        VBox vBox = new VBox(start, rank, chat, profile, logOut);
        if (isHost) {
            vBox.getChildren().add(setPrice);
            logOut.toFront();
        }
        vBox.setId("menu");
        vBox.setAlignment(Pos.CENTER);
        vBox.relocate(400, 300);
        vBox.translateXProperty().bind(vBox.widthProperty().divide(2).negate());
        vBox.translateYProperty().bind(vBox.heightProperty().divide(2).negate());
        root.getChildren().addAll(vBox);

    }

    private Label setUpChangePrices() {
        Label setPrice = new Label("SET PRICE");
        setPrice.setId("label_button");
        setPrice.setOnMouseClicked(mouseEvent -> {
            VBox vBox = new VBox();
            HBox select = new HBox();
            TextField price = new TextField();
            price.setMaxWidth(100);
            TextField itemName = new TextField();
            price.setId("inputBox");
            itemName.setId("inputBox");
            itemName.setDisable(true);
            itemName.setMaxWidth(100);
            ImageView currentItem = new ImageView();
            Label send = new Label("Send");
            send.setId("label_button");
            Label back = new Label("back");
            back.setId("label_button");
            select.setSpacing(20);
            Pop pop = new Pop(vBox, view.getSnap(), root, Pop.AddType.BUTTONS_TEXT);

            back.setOnMouseClicked(event -> root.getChildren().remove(pop.getStackPane()));
            send.setOnMouseClicked(mouseEvent1 -> sendItemPrice(price, itemName, pop));
            price.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER)
                    sendItemPrice(price, itemName, pop);
            });
            select.getChildren().addAll(currentItem, itemName, price, send);
            FlowPane items = new FlowPane();
            for (String item : itemList) {
                ImageView itemImage = Images.getImageForGoal(item);
                itemImage.setFitWidth(50);
                itemImage.setFitHeight(50);
                itemImage.setOnMouseClicked(mouseEvent1 -> {
                    currentItem.setImage(itemImage.getImage());
                    itemName.setText(item);
                });
                items.getChildren().add(itemImage);
            }
            vBox.getChildren().addAll(items, select, back);
        });
        return setPrice;
    }

    private Label setUpLogOut() {
        Label logOut = new Label("LOGOUT");
        logOut.setId("label_button");
        logOut.setOnMouseClicked(event -> {
            client.closلeSocket();
            view.setRoot(new Menu(view).getRoot());
        });
        return logOut;
    }

    private Label setUpProfile() {
        Label profile = new Label("PROFILE");
        profile.setId("label_button");
        profile.setOnMouseClicked(event -> client.getPerson(client.getMyId()));
        return profile;
    }

    private Label setUpRank() {
        Label rank = new Label("RANK");
        rank.setId("label_button");
        rank.setOnMouseClicked(event -> view.setRoot(client.getScoreboard().getRoot()));
        return rank;
    }

    private Label setUpStart() {
        Label start = new Label("START");
        start.setId("label_button");
        start.setOnMouseClicked(event -> view.setRoot(new LevelSelect(view).getRoot()));
        return start;
    }

    private Label setUpChat() {
        Label chat = new Label("CHAT");
        chat.setId("label_button");
        chat.setOnMouseClicked(event -> view.setRoot(client.getChatroom().getRoot()));
        return chat;
    }

    private void sendItemPrice(TextField price, TextField itemName, Pop pop) {
        if (!itemName.getText().equals("")) {
            try {
                client.setPrice(itemName.getText(), Integer.parseInt(price.getText()));
                root.getChildren().remove(pop.getStackPane());
                new Pop(new Label("Price Sent"), view.getSnap(), root, Pop.AddType.ALERT);
            } catch (Exception e) {
                new Pop(new Label("Invalid Request"), view.getSnap(), root, Pop.AddType.ALERT);
            }
        }
    }

    public Group getRoot() {
        return root;
    }
}
