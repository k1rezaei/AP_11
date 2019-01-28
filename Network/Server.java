import com.google.gson.Gson;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Server {

    private static final String end = "#";
    private static final String DATA_CHAT_ROOM = "data_chat_room";
    private static final String DATA_SCOREBOARD = "data_scoreboard";
    private static final String DATA_ITEM_COST = "data_item_cost";
    private static final String BOUGHT_ITEM = "bought_item";

    ArrayList<Profile> profiles = new ArrayList<>();
    private Server me;
    ArrayList<Talk> talks = new ArrayList<>();

    Map<String, Integer> items = new HashMap<>(), price = new HashMap<>();

    public ArrayList<Talk> getTalks() {
        return talks;
    }

    public Server() {
        me = this;
    }

    Task<Void> task = new Task<Void>() {
        @Override
        public Void call() throws IOException {
            int cnt = 8051;
            while (true) {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(8050);
                    Socket socket = serverSocket.accept();
                    System.err.println("connected new user");
                    Formatter formatter = new Formatter(socket.getOutputStream());
                    Scanner scanner = new Scanner(socket.getInputStream());
                    String id = scanner.nextLine();
                    System.err.println("userid is : " + id);
                    if (validId(id)) {
                        System.err.println("valid");
                        formatter.format("userName Valid\n");
                        formatter.flush();
                    } else {
                        System.err.println("not valid");
                        formatter.format("userName inValid\n");
                        formatter.flush();
                        socket.close();
                        continue;
                    }

                    formatter.format(Integer.toString(cnt) + '\n');
                    System.err.println("port is " + cnt);
                    formatter.flush();

                    System.err.println("waiting for finishing");
                    String connected = scanner.nextLine();
                    System.err.println("connected");

                    formatter.close();
                    scanner.close();

                    serverSocket.close();
                    serverSocket = new ServerSocket(cnt);
                    cnt++;
                    socket = serverSocket.accept();
                    System.err.println("User adding");

                    Profile profile = new Profile(new Person(id, id), socket);
                    profiles.add(profile);
                    profile.setServer(me);

                    profile.run();

                    System.err.println("User added");

                } catch (Exception e) {
                    System.err.println("Server's problem");
                    e.printStackTrace();
                }
            }
        }
    };

    void run() {
        new Thread(task).start();
    }

    private boolean validId(String id) {
        for (Profile profile : profiles)
            if (id.equals(profile.getPerson().getId())) return false;
        return true;
    }

    synchronized public void addMessageToChatRoom(Talk talk) {
        talks.add(talk);
        String command = getChatRoom();
        for (Profile profile : profiles) {
            profile.command(command);
        }
    }


    synchronized public void updateScoreboard() {
        String scoreboard = getScoreboard();
        for (Profile profile : profiles)
            profile.command(scoreboard);
    }


        public String getScoreboard() {
        ArrayList<Person> people = new ArrayList<>();
        for (Profile profile : profiles)
            people.add(profile.getPerson());
        Gson gson = new Gson();
        return DATA_SCOREBOARD + '\n' + gson.toJson(people.toArray()) + '\n' + end + '\n';
    }

    public String getChatRoom() {
        Gson gson = new Gson();
        return DATA_CHAT_ROOM + '\n' + gson.toJson(talks.toArray()) + '\n' + end + '\n';
    }

    public String getItemCost(String item) {
        return DATA_ITEM_COST + '\n' + item + '\n' + price.get(item) + '\n' + end + '\n';
    }

    public synchronized String buyItem(String item) {
        if(items.get(item) != null && items.get(item) > 0) {
            int count = items.get(item);
            count --;
            items.put(item, count);
            return BOUGHT_ITEM + "\n" + item + "\n" + end + "\n";
        }
        return "";
    }

    //todo initialize item list.
}
