public class Talk {
    String sender, recipient;
    String text, repliedText = null;

    public Talk(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public Talk(String sender, String text, String reciever) {
        this.sender = sender;
        this.text = text;
        this.recipient = reciever;
    }

    public void setRepliedText(String repliedText) {
        this.repliedText = repliedText;
    }

    public String getText() {return text;}
    public String getSender() {return sender;}
    public String getRecipient() {
        return recipient;
    }
    public String getRepliedText() {
        return repliedText;
    }
}
