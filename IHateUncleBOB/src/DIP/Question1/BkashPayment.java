package DIP.Question1;

import java.sql.SQLOutput;

public class BkashPayment implements PaymentProcessor{
    @Override
    public void payment(double amount) {
        IO.print("Paid using bkash payment");
    }
}
