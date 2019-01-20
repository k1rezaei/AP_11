import javafx.scene.image.Image;

import java.util.ArrayList;

public class LoadedImage {

    ArrayList<Image> images;


    ArrayList<Integer> count = new ArrayList<>(), column = new ArrayList<>(), width = new ArrayList<>(), height = new ArrayList<>();



    public LoadedImage(ArrayList<Image> images, int count, int[] column) {
        this.images = images;

        for (int i = 0; i < images.size(); i++) {
            this.count.add(count);
            this.column.add(column[i]);
        }

        for (Image image : images) {
            width.add((int) image.getWidth());
            height.add((int) image.getHeight());
        }
    }

}
