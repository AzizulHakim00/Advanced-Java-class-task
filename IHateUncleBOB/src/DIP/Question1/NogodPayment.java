package DIP.Question1;

public class NogodPayment implements PaymentProcessor{
    @Override
    public void payment(double amount) {
        IO.print("Paid using Nogod payment");
    }
}
