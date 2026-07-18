package OCP.Question1;

public class PremiumCustomerDiscount implements DiscountStrategy {
    @Override
    public double calculateDiscount(double amount) {
        return amount*0.20; //20% discount deya hobe
    }
}
