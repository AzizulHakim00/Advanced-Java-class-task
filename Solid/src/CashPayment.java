public class CashPayment implements PaymentInterface {

    @Override
    public void pay() {
        IO.println("Cash Payment!");
    }
}
