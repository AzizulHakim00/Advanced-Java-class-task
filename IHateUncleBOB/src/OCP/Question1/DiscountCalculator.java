package OCP.Question1;

public class DiscountCalculator {
    private DiscountStrategy discountStrategy;

    public DiscountCalculator(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }
    public double calculateDiscount(double amount) {
        return discountStrategy.calculateDiscount(amount);
    }
}
