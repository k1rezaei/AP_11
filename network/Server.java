import com.google.gson.Gson;
import javafx.concurrent.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {

    private static final String end = "#";
    private static final String DATA_CHAT_ROOM = "data_chat_room";
    private static final String DATA_SCOREBOARD = "data_scoreboard";
    private static final String DATA_ITEM_COST = "data_item_cost";
    private static final String BOUGHT_ITEM = "bought_item";
    private static final String SOLD_ITEM = "sold_item";
    private static final String DATA_INBOX = "data_inbox";
    private static final String DATA_FRIENDS = "data_friends";
    private static final String DATA_PERSON = "data_person";
    private static final String DATA_WAREHOUSE = "data_warehouse";
    private static final int BASE_NUMBER_OF_ITEMS = 4;

    private ArrayList<Profile> profiles = new ArrayList<>();
    private Server me;
    private ArrayList<Talk> talks = new ArrayList<>();
    private int port;
    private HashMap<String, Integer> items, prices = new HashMap<>();
    private Gson gson = new Gson();
    private String[] itemList;

    private ArrayList<Person> users = new ArrayList<>();
    private ArrayList<TeamGame> teamGames = new ArrayList<>();

    private Task<Void> task = new Task<Void>() {
        @Override
        public Void call() throws IOException {
            while (true) {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(port);
                    Socket socket = serverSocket.accept();
                    System.err.println("connected new user");
                    Formatter formatter = new Formatter(socket.getOutputStream());
                    Scanner scanner = new Scanner(socket.getInputStream());
                    String id = scanner.nextLine();
                    System.err.println("userid is : " + id);

                    int status = validId(id);

                    if (status == 2) {
                        System.err.println("valid and new");
                        formatter.format("userName Valid\n");
                        formatter.flush();
                    } else if (status == 0) {
                        System.err.println("not valid");
                        formatter.format("userName inValid\n");
                        formatter.flush();
                        socket.close();
                        continue;
                    } else if (status == 1) {
                        System.err.println("valid but old");
                        formatter.format("userName Valid\n");
                        formatter.flush();
                    }

                    int port = Integer.parseInt(scanner.nextLine());

                    formatter.close();
                    scanner.close();

                    serverSocket.close();
                    serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                    System.err.println("User adding");

                    Profile profile;
                    if (status == 2) profile = new Profile(new Person(id, id), socket);
                    else {
                        profile = new Profile(getPersonByIdInData(id), socket);
                    }
                    profiles.add(profile);
                    if (!users.contains(profile.getPerson())) users.add(profile.getPerson());
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
    private Random random = new Random();


    public Server(int port) {
        me = this;
        items = new HashMap<>();
        this.port = port;
        initialize();
    }

    public ArrayList<Talk> getTalks() {
        return talks;
    }

    private void initialize() {
        itemList = new String[]{"Adornment", "CheeseFerment", "Cookie", "Souvenir",
                "bear", "Cheese", "Horn",
                "BrightHorn", "ColoredPlume", "Intermediate",
                "Curd", "lion",
                "Egg", "Milk",
                "EggPowder", "Plume",
                "Cake", "Fabric", "Sewing", "Varnish",
                "CarnivalDress", "Flour", "SourCream", "Wool"};


        try {
            InputStream inputStream = new FileInputStream("data.txt");
            Scanner scanner = new Scanner(inputStream);
            users.addAll(Arrays.asList(gson.fromJson(scanner.nextLine(), Person[].class)));
            talks.addAll(Arrays.asList(gson.fromJson(scanner.nextLine(), Talk[].class)));

            String[] _items = gson.fromJson(scanner.nextLine(), String[].class);
            Integer[] _counts = gson.fromJson(scanner.nextLine(), Integer[].class);
            Integer[] _prices = gson.fromJson(scanner.nextLine(), Integer[].class);

            for (int i = 0; i < _items.length; i++) {
                this.items.put(_items[i], _counts[i]);
                this.prices.put(_items[i], _prices[i]);
            }

            scanner.close();
            inputStream.close();
        } catch (Exception e) {
            System.err.println("Can Not Find Data File Saved By Server.");
            for (String item : itemList) {
                this.items.put(item, 10);
                this.prices.put(item, 200);
            }
        }

    }

    private Person getPersonByIdInData(String id) {
        for (Person person : users)
            if (person.getId().equals(id)) return person;
        return null;
    }

    void run() {
        new Thread(task).start();
    }

    private int validId(String id) {
        for (Profile profile : profiles)
            if (id.equals(profile.getPerson().getId())) return 0;
        for (Person person : users)
            if (person.getId().equals(id)) return 1;
        return 2;
    }

    synchronized void addMessageToChatRoom(Talk talk) {
        talks.add(talk);
        String command = getChatRoom();
        for (Profile profile : profiles) {
            profile.command(command);
        }
        saveData();

    }


    synchronized public void updateScoreboard() {
        saveData();
        String scoreboard = getScoreboard();
        for (Profile profile : profiles)
            profile.command(scoreboard);
    }


    synchronized public String getScoreboard() {
        ArrayList<Person> people = new ArrayList<>();
        for (Profile profile : profiles)
            people.add(profile.getPerson());
        return DATA_SCOREBOARD + '\n' + gson.toJson(people.toArray()) + '\n' + end + '\n';
    }

    synchronized public String getChatRoom() {
        return DATA_CHAT_ROOM + '\n' + gson.toJson(talks.toArray()) + '\n' + end + '\n';
    }

    synchronized public String getItemCost(String item) {
        return DATA_ITEM_COST + '\n' + item + '\n' + prices.get(item) + '\n' + end + '\n';
    }

    synchronized public String buyItem(String item) {
        if (items.get(item) != null && items.get(item) > 0) {
            int count = items.get(item);
            count--;
            items.put(item, count);
            updateWarehouse();
            return BOUGHT_ITEM + "\n" + item + "\n" + prices.get(item) + "\n" + end + "\n";
        }
        return "";
    }

    synchronized public int getCost(ArrayList<String> goals) {
        int cost = 0;
        for (String item : goals)
            if (prices.get(item) != null)
                cost += prices.get(item);
        cost *= 3;
        return cost;
    }

    synchronized public void remove(Person person) {

        for (Profile profile : profiles) {
            profile.getPerson().removeFollowings(person);
            profile.getPerson().removeFollowers(person);
            profile.getPerson().removeFriends(person);
        }

        for (Profile profile : profiles) {
            if (profile.getPerson().equals(person)) {
                profiles.remove(profile);
                //todo host save kone adam haro. users.remove(profile.getPerson());
                break;
            }
        }

        saveData();

        updateScoreboard();
    }

    synchronized public String sellItem(String item) {
        int count = 0;
        if (items.get(item) != null) count = items.get(item);
        count++;
        items.put(item, count);
        updateWarehouse();
        return SOLD_ITEM + "\n" + item + "\n" + prices.get(item) + "\n" + end + "\n";
    }

    synchronized public void sendPrivateMessage(String senderId, String receiverId, String text) {
        Person sender = getPerson(senderId);
        Person receiver = getPerson(receiverId);

        Talk talk = new Talk(senderId, text, receiverId);
        sender.addToInbox(talk);
        receiver.addToInbox(talk);

        saveData();

        command(updateInbox(senderId), senderId);
        command(updateInbox(receiverId), receiverId);
    }

    synchronized public String updateInbox(String id) {
        Person p = getPerson(id);
        return DATA_INBOX + "\n" + gson.toJson(p.getInbox().toArray(new Talk[0])) + "\n" + end + "\n";
    }

    synchronized public Person getPerson(String id) {
        for (Profile profile : profiles)
            if (profile.getPerson().getId().equals(id)) {
                return profile.getPerson();
            }
        return null;
    }

    synchronized public void addFriendRequest(String id1, String id2) {
        Person follower = getPerson(id1);
        Person following = getPerson(id2);
        if (follower.getFollowings().contains(id2)) return;
        if (follower.getFriends().contains(id2)) return;

        follower.addFollowings(following);
        following.addFollowers(follower);

        saveData();

        command(updateFriends(id1), id1);
        command(updateFriends(id2), id2);
    }

    synchronized public void acceptFriendRequest(String id1, String id2) {
        Person follower = getPerson(id2);
        Person following = getPerson(id1);

        if (!follower.getFollowings().contains(id1)) return;

        follower.removeFollowings(following);
        following.removeFollowers(follower);

        follower.addFriend(following);
        following.addFriend(follower);

        saveData();

        command(updateFriends(id1), id1);
        command(updateFriends(id2), id2);
    }


    public String updateFriends(String id) {
        Person p = getPerson(id);
        String command = DATA_FRIENDS + "\n" +
                gson.toJson(p.getFollowers().toArray(new String[0])) + "\n" +
                gson.toJson(p.getFriends().toArray(new String[0])) + "\n" +
                gson.toJson(p.getFollowings().toArray(new String[0])) + "\n" +
                end + "\n";
        return command;
    }

    synchronized public void command(String command, String id) {
        for (Profile profile : profiles)
            if (profile.getPerson().getId().equals(id)) {
                profile.command(command);
                return;
            }

    }

    public String getPersonCommand(String id) {
        Person person = getPerson(id);
        String command = DATA_PERSON + "\n" + id + "\n" + gson.toJson(person) + "\n" + end + "\n";
        return command;
    }

    synchronized public String getWarehouse() {

        String command = DATA_WAREHOUSE + "\n" +
                gson.toJson(items) + "\n" +
                gson.toJson(prices) + "\n" +
                end + "\n";
        return command;
    }

    synchronized public void updateWarehouse() {
        String command = getWarehouse();
        sendToAll(command);
    }

    synchronized private void sendToAll(String command) {
        for (Profile profile : profiles)
            profile.command(command);
    }

    private void saveData() {
        try {
            OutputStream outputStream = new FileOutputStream("data.txt");
            Formatter formatter = new Formatter(outputStream);
            formatter.format(gson.toJson(users.toArray()) + "\n");
            formatter.format(gson.toJson(talks.toArray()) + "\n");
            formatter.format(gson.toJson(items.keySet().toArray()) + "\n");
            formatter.format(gson.toJson(items.values().toArray()) + "\n");

            ArrayList<Integer> temp = new ArrayList<>();
            for (String type : items.keySet())
                temp.add(prices.get(type));
            formatter.format(gson.toJson(temp.toArray()));

            formatter.flush();
            formatter.close();
            outputStream.close();
        } catch (Exception e) {
            System.err.println("Can Not Save File In Data");
        }
    }

    synchronized public void changeCost(String type, int cost) {
        prices.put(type, cost);
        updateWarehouse();
        saveData();
    }

    synchronized public ArrayList<TeamGame> getTeamGames() {
        return teamGames;
    }

    public ArrayList<String> getRandomGoals() {
        int number = random.nextInt(5) + BASE_NUMBER_OF_ITEMS, size = itemList.length;
        ArrayList<String> goals = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int c = random.nextInt(size);
            goals.add(itemList[c]);
        }
        return goals;
    }

    //todo initialize item list.
}
