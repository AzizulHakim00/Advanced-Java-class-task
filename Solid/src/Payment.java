public class Payment {
    public void pay(String type)
    {
        if(type.equals("Cash")){
            IO.println("Cash Payment!");
        }
        else if(type.equals("CreditCard")){
            IO.println("Credit Card Payment!");
        }
        //wrong approach
        else if (type.equals("Bkash")) {
            IO.println("Bkash Payment!");

        }
    }
}
