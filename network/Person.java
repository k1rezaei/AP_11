import java.util.ArrayList;
import java.util.Objects;

public class Person {
    private String id, name, level = "1";
    private int money = 0;

    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> followings = new ArrayList<>();
    private ArrayList<String> followers = new ArrayList<>();



    private ArrayList<String> teamGames = new ArrayList<>();

    private ArrayList<Talk> inbox = new ArrayList<>();

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    synchronized public void addTeamGameWith(String id) {
        teamGames.add(id);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    synchronized public void addToInbox(Talk talk) {
        inbox.add(talk);
    }

    synchronized public void addFriend(Person person) {
        String id = person.getId();
        if (friends.contains(id)) return;
        friends.add(id);
    }

    synchronized public void addFollowings(Person person) {
        String id = person.getId();
        if (followings.contains(id)) return;
        followings.add(id);
    }

    synchronized public void addFollowers(Person person) {
        String id = person.getId();
        if (followers.contains(id)) return;
        followers.add(id);
    }

    synchronized public void removeFollowers(Person person) {
        followers.remove(person.getId());
    }

    synchronized public void removeFollowings(Person person) {
        followings.remove(person.getId());
    }

    synchronized public void removeFriends(Person person) {
        friends.remove(person.getId());
    }

    synchronized public ArrayList<String> getFriends() {
        return friends;
    }

    synchronized public ArrayList<String> getFollowings() {
        return followings;
    }

    synchronized public ArrayList<String> getFollowers() {
        return followers;
    }

    synchronized public ArrayList<String> getTeamGames() { return teamGames; }

    public ArrayList<Talk> getInbox() {
        return inbox;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) return false;
        return ((Person) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

