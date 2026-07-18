package OCP.Question1;

public class VipCustomerDiscount implements DiscountStrategy {

    @Override
    public double calculateDiscount(double amount) {
        return amount*0.10;
    }
}
