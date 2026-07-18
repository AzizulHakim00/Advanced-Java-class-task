package DIP.Question1;

public class PaymentService {
    private PaymentProcessor paymentProcessor;

    public PaymentService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
    public void makePayment(double amount) {
        paymentProcessor.payment(amount);
    }
}
