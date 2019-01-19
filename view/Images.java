import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Images {

    private static HashMap<String, LoadedImage> map = new HashMap<>();
    private final static String BASE = "file:textures/";

    public static void init() {


        //added 0->3 Animals

        loadImage("cat", 4, 24, new int[] {6, 4, 6, 4});

        loadImage("chicken", 5, 24, new int[] {5, 5, 5, 5, 5});

        loadImage("sheep", 5, 24, new int[] {5, 4, 5, 4, 4});

        loadImage("cow", 5, 24, new int[] {4, 3, 3, 3, 4});

        loadImage("dog", 4, 24, new int[] {6, 6, 6, 6});

        loadImage("lion", 4, 24, new int[] {6, 3, 5, 3});

        loadImage("bear", 4, 24, new int[]{4, 4, 4, 4});


        String[] items = new String[] {"Adornment.png", "CheeseFerment.png", "FlouryCake.png", "Souvenir.png",
                "Bear.png", "Cheese.png", "Horn.png", "SpruceBrownBear.png",
                "BrightHorn.png", "ColoredPlume.png", "Intermediate.png", "SpruceGrizzly.png",
                "CagedJaguar.png", "Curd.png", "MegaPie.png", "SpruceJaguar.png",
                "CagedLion.png", "Egg.png", "Milk.png", "SpruceLion.png",
                "CagedWhiteBear.png", "EggPowder.png", "Plume.png", "SpruceWhiteBear.png",
                "Cake.png Fabric.png", "Sewing.png", "Varnish.png",
                "CarnivalDress.png", "Flour.png", "SourCream.png", "Wool.png"};

        for (int i=0; i<items.length; i++) {
            String type = items[i].split(".")[0];
            ArrayList<Image> images = new ArrayList<>();
            images.add(new Image(BASE + "item/" + items[i]));
            loadImage(type, images,1, new int[]{1});
        }

        //loadImage("plant", images, 1, new int[]{1});

        loadImage("helicopter", 4, 1, new int[] {1, 1, 1, 1});

        loadImage("truck", 4, 1, new int[] {1, 1, 1, 1});

        loadImage("warehouse", 4, 1, new int[] {1, 1, 1, 1});

        loadImage("well", 4, 16, new int[] {4, 4, 4, 4});

        for (int i=0; i<6; i++)
            loadImage("workshop" + i, 5, 16, new int[] {4, 4, 4 , 4, 4});

    }

    static void addImages(ArrayList<Image> images, String type, int n) {
        String address = BASE;
        if(type.startsWith("workshop"))
            address = BASE + "workshop/" + type.charAt(type.length() - 1) + "/";
        else address = BASE + type + "/";

        for (int i=0; i<n; i++)
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

    SpriteAnimation getSpriteAnimation(Entity entity) {
        return new SpriteAnimation(map.get(entity.getType()));
    }

    ImageView getIcon(Entity entity) {
        return new ImageView(new Image(BASE + "ui/Icons/" + entity.getType() + ".png"));
    }

}


/*Image image = new Image(new File(path).toURI().toString(), 150, 100, false, false);
    ImageView imageView = new ImageView(image);
    */
