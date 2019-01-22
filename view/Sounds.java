import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class Sounds {

    private static HashMap<String, MediaPlayer> mediaPlayerHashMap = new HashMap<>();

    static void add(String type){


        Media sound = new Media(new File("sounds/"+type+"_voice.mp3").toURI().toString());
        mediaPlayerHashMap.put(type + "_voice",new MediaPlayer(sound));

        Media sound2 = new Media(new File("sounds/"+type+"_die.mp3").toURI().toString());
        mediaPlayerHashMap.put(type + "_die",new MediaPlayer(sound2));

    }

    public static void init() {
        add("chicken");
        add("sheep");
        add("cow");
        add("bear");
        add("lion");
        add("dog");
        add("cat");
    }

    public static void play(String type){
        mediaPlayerHashMap.get(type).stop();
        mediaPlayerHashMap.get(type).play();
    }
}
