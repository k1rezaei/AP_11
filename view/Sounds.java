import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Sounds {

    private static HashMap<String, MediaPlayer> mediaPlayerHashMap = new HashMap<>();

    private static void add(String type) {

        Media sound = new Media(new File("sounds/" + type + "_voice.mp3").toURI().toString());
        mediaPlayerHashMap.put(type + "_voice", new MediaPlayer(sound));

        Media sound2 = new Media(new File("sounds/" + type + "_die.mp3").toURI().toString());
        mediaPlayerHashMap.put(type + "_die", new MediaPlayer(sound2));

    }

    public static void init() {
        mediaPlayerHashMap.clear();
        add("chicken");
        add("sheep");
        add("cow");
        add("bear");
        add("lion");
        add("dog");
        add("cat");
        Media sound = new Media(new File("sounds/main_theme.mp3").toURI().toString());
        mediaPlayerHashMap.put("main_theme", new MediaPlayer(sound));
    }

    public static void mute() {
        for (Map.Entry<String, MediaPlayer> entry : mediaPlayerHashMap.entrySet()) {
            entry.getValue().stop();
        }
    }

    public static void play(String type) {
        mediaPlayerHashMap.get(type).stop();
        mediaPlayerHashMap.get(type).play();
    }

    public static MediaPlayer get(String type) {
        return mediaPlayerHashMap.get(type);
    }


}
