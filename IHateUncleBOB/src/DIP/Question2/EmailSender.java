package DIP.Question2;

public class EmailSender implements MsgSender{
    @Override
    public void send(String message) {
        System.out.println("Email sending to "+message);
    }
}
