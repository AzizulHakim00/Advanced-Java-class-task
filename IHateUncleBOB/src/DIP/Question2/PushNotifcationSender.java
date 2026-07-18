package DIP.Question2;

public class PushNotifcationSender implements MsgSender {
    public void send(String message) {
        System.out.println("Push Notification sending to "+message);
    }
}
