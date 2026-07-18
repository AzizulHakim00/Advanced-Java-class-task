public class BkashPayment implements PaymentInterface {
    @Override
    public void pay() {
        IO.println("Bkash Payment!");
    }
}
