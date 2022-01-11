package il.ac.hit.costmanager;

/**
 * Message a message which passes to the UI
 */
public class Message {
    private String messageContent;

    /**
     * Message constructor
     * @param messageContent the message content in string
     */
    public Message(String messageContent) {
        setMessageContent(messageContent);
    }

    /**
     * messageContent getter
     * @return the message content string
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * messageContent setter
     * @param messageContent the message content string
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
