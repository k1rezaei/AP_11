import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Person {
    String id, name, level = "1";

    private Set<Person> friends = new HashSet<>();
    private Set<Person> followings = new HashSet<>();
    private Set<Person> followers = new HashSet<>();

    private ArrayList<Talk> inbox = new ArrayList<>();


    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    synchronized public void addToInbox(Talk talk) {
        inbox.add(talk);
    }

    synchronized public void addFriend(Person person) {
        friends.add(person);
    }

    synchronized public void addFollowings(Person person) {
        followings.add(person);
    }

    synchronized public void addFollowers(Person person) {
        followers.add(person);
    }

    synchronized public void removeFollowers(Person person) {
        followers.remove(person);
    }
    
    synchronized public void removeFollowing(Person person) {
        followings.remove(person);
    }

    synchronized public Set<Person> getFriends() {return friends;}
    synchronized public Set<Person> getFollowings() {return followings;}
    synchronized public Set<Person> getFollowers(){ return followers;}

    public ArrayList<Talk> getInbox() { return inbox;}

    public String getId() { return  id; }

    public String getName() { return name; }

    public String getLevel() { return level; }

    public void setLevel(String level) {this.level = level;}


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Person)) return false;
        return ((Person) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

