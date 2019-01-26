import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Images {

    private final static String BASE = "file:textures/";
    private static HashMap<String, LoadedImage> map = new HashMap<>();

    public static void init() {


        //added 0->3 Animals

        loadImage("cat", 4, 24, new int[]{6, 4, 6, 4});

        loadImage("chicken", 6, 24, new int[]{5, 5, 5, 5, 5, 5});

        loadImage("sheep", 6, 24, new int[]{5, 4, 5, 4, 4, 4});

        loadImage("cow", 6, 24, new int[]{4, 3, 3, 3, 3, 4});

        loadImage("dog", 5, 24, new int[]{6, 6, 6, 6, 5});

        loadImage("lion", 4, 24, new int[]{6, 3, 5, 3});

        loadImage("bear", 4, 24, new int[]{4, 4, 4, 4});


        String[] items = new String[]{"Adornment.png", "CheeseFerment.png", "Cookie.png", "Souvenir.png",
                "Bear.png", "Cheese.png", "Horn.png", "SpruceBrownBear.png",
                "BrightHorn.png", "ColoredPlume.png", "Intermediate.png", "SpruceGrizzly.png",
                "CagedJaguar.png", "Curd.png", "MegaPie.png", "SpruceJaguar.png",
                "CagedLion.png", "Egg.png", "Milk.png", "SpruceLion.png",
                "CagedWhiteBear.png", "EggPowder.png", "Plume.png", "SpruceWhiteBear.png",
                "Cake.png", "Fabric.png", "Sewing.png", "Varnish.png",
                "CarnivalDress.png", "Flour.png", "SourCream.png", "Wool.png"};

        for (int i = 0; i < items.length; i++) {
            String type = items[i].split("\\.")[0];
            ArrayList<Image> images = new ArrayList<>();
            images.add(new Image(BASE + "item/" + items[i]));
            loadImage(type, images, 1, new int[]{1});
        }


        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image(BASE + "plant/plant.png"));
        loadImage("plant", images, 1, new int[]{1});

        loadImage("helicopter", 4, 1, new int[]{1, 1, 1, 1});

        loadImage("truck", 4, 1, new int[]{1, 1, 1, 1});

        loadImage("warehouse", 4, 1, new int[]{1, 1, 1, 1});

        loadImage("well", 4, 16, new int[]{4, 4, 4, 4});

        for (int i = 0; i < 6; i++)
            loadImage("workshop" + i, 5, 16, new int[]{4, 4, 4, 4, 4});
        loadImage("helicopterMini", 4, 6, new int[]{3, 3, 3, 3});
        loadImage("truckMini", 4, 2, new int[]{2, 2, 2, 2});
    }

    static void addImages(ArrayList<Image> images, String type, int n) {
        String address;
        if (type.startsWith("workshop"))
            address = BASE + "workshops/" + type.charAt(type.length() - 1) + "/";
        else address = BASE + type + "/";

        for (int i = 0; i < n; i++)
            images.add(new Image(address + i + ".png"));
    }

    static void loadImage(String type, int n, int count, int[] column) {
        ArrayList<Image> images = new ArrayList<>();
        addImages(images, type, n);
        map.put(type, new LoadedImage(images, count, column));
    }

    static void loadImage(String type, ArrayList<Image> images, int count, int[] column) {
        map.put(type, new LoadedImage(images, count, column));
    }

    static SpriteAnimation getSpriteAnimation(Entity entity) {
        return new SpriteAnimation(map.get(entity.getType()));
    }

    static SpriteAnimation getSpriteAnimation(String type) {
        return new SpriteAnimation(map.get(type));
    }

    static ImageView getIcon(Entity entity) {
        return new ImageView(new Image(BASE + "ui/Icons/Products/" + entity.getType() + ".png"));
    }

    static ImageView getIcon(String type) {
        return new ImageView(new Image(BASE + "ui/Icons/Products/" + type + ".png"));
    }
}
