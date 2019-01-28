public class Talk {
    Person sender;
    String text;

    public Talk(Person sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getText() {return text;}
    public Person getSender() {return sender;}

}
