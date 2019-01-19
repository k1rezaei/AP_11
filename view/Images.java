
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;

public class Images
    public void init(){
        try {
            Image image = new Image(new File(path));
        }catch (IOException e){

        }
    }
}