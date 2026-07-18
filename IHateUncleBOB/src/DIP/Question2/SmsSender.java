package DIP.Question2;

public class SmsSender implements MsgSender{
    @Override
    public void send(String message) {
        System.out.println("SMS sending to "+message);
    }
}
