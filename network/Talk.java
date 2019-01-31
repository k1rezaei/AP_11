public class Talk {
    private String sender, recipient;
    private String text, repliedText = null;

    public Talk(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public Talk(String sender, String text, String recipient) {
        this.sender = sender;
        this.text = text;
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getRepliedText() {
        return repliedText;
    }

    public void setRepliedText(String repliedText) {
        this.repliedText = repliedText;
    }
}
