public class Person {
    String id, name, level = "1";

    //ArrayList<Person> Friends;
    //ArrayList<Person> Following;
    //ArrayList<Person> Follower;


    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return  id; }

    public String getName() { return name; }

    public String getLevel() { return level; }

    public void setLevel(String level) {this.level = level;}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Person)) return false;
        return ((Person) obj).getId() == getId();
    }
}

