import javafx.scene.image.Image;

import java.util.ArrayList;

public class LoadedImage {

    private ArrayList<Image> images;
    private ArrayList<Integer> count = new ArrayList<>(), column = new ArrayList<>(), width = new ArrayList<>(), height = new ArrayList<>();


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

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<Integer> getCount() {
        return count;
    }

    public void setCount(ArrayList<Integer> count) {
        this.count = count;
    }

    public ArrayList<Integer> getColumn() {
        return column;
    }

    public void setColumn(ArrayList<Integer> column) {
        this.column = column;
    }

    public ArrayList<Integer> getWidth() {
        return width;
    }

    public void setWidth(ArrayList<Integer> width) {
        this.width = width;
    }

    public ArrayList<Integer> getHeight() {
        return height;
    }

    public void setHeight(ArrayList<Integer> height) {
        this.height = height;
    }
}
