package DIP.Question2;

public class Notification {
    private MsgSender msgSender;

    public Notification(MsgSender msgSender) {
        this.msgSender = msgSender;
    }
    public void notifyUser(String message) {
        msgSender.send(message);
    }
}
