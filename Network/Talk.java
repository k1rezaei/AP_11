public class Talk {
    Person sender, recipient;
    String text;

    public Talk(Person sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public Talk(Person sender, String text, Person reciever) {
        this.sender = sender;
        this.text = text;
        this.recipient = reciever;
    }

    public String getText() {return text;}
    public Person getSender() {return sender;}
    public Person getRecipient() {
        return recipient;
    }
}
