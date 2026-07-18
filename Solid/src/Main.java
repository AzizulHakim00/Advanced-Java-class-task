void main()
{
    IO.println("Hello to SOLID");

    Payment payment = new Payment();
    payment.pay("Cash");
    payment.pay("CreditCard");

    //right apporach (OCP)
    PaymentInterface cashPayment = new CashPayment();
    cashPayment.pay();

    PaymentInterface bkashPayment = new BkashPayment();
    bkashPayment.pay();

    Switch s1 = new Switch(new AppleBulb());
    s1.press();
}

//S = Single Responsibility Principle (SRP)
//O = Open Close Princile (OCP)
//L = Liskov Substituion Principle (LSP)
//I = Interface Segregation Principle (ISP)
//D = Dependency Inversible Principle (DIP) : Boro Code Choto Code er upor depend hobena
// Onner upor deppended kaj korbena...like radhuni and bazar er lok
