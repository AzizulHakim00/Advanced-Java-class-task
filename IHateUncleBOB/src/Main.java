import DIP.Question1.BkashPayment;
import DIP.Question1.NogodPayment;
import DIP.Question1.PaymentProcessor;
import DIP.Question1.PaymentService;
import DIP.Question2.EmailSender;
import DIP.Question2.Notification;
import DIP.Question2.PushNotifcationSender;
import DIP.Question2.SmsSender;
import ISP.Question1.BasicPrinter;
import ISP.Question1.MultfunctionalPrinter;
import ISP.Question2.Airplane;
import ISP.Question2.Car;
import LSP.Question1.Penguin;
import LSP.Question1.Sparrow;
import LSP.Question2.Square;
import OCP.Question1.DiscountCalculator;
import OCP.Question1.PremiumCustomerDiscount;
import OCP.Question1.RegularCustomerDiscount;
import OCP.Question1.VipCustomerDiscount;
import OCP.Question2.Circle;
import OCP.Question2.Rectangle;
import OCP.Question2.Shape;
import OCP.Question2.Triangle;
import SRP.Question1.Employee;
import SRP.Question1.ReportGenerator;
import SRP.Question1.SalaryCalculator;
import SRP.Question2.Book;
import SRP.Question2.BookPrinter;

void main() {

    System.out.println("SRP");
    Employee employee = new Employee("Safayet Ullah",19, 1000000);
    SalaryCalculator calculator = new SalaryCalculator();

    double salary = calculator.calculateSalary(employee, 200000);

    ReportGenerator generator = new ReportGenerator();

    generator.generateReport(employee,  salary);

    //======================================
    Book book = new Book("Amar Bangla Boi", "Safayet Ullah", "120Safa");
    BookPrinter bookPrinter = new BookPrinter();
    bookPrinter.printBook(book);

    ///OCP
    System.out.println("\nOCP");
    DiscountCalculator regular = new DiscountCalculator(new RegularCustomerDiscount());
    DiscountCalculator premium = new DiscountCalculator(new PremiumCustomerDiscount());
    DiscountCalculator vip = new DiscountCalculator(new VipCustomerDiscount());
    IO.println("Regular Discount: " + regular.calculateDiscount(500) + "tk");
    IO.println("Premium Discount: " + premium.calculateDiscount(500) + "tk");
    IO.println("Vip Discount: " + vip.calculateDiscount(500) + "tk");

    Shape circle = new Circle(5);
    Shape rectangle = new Rectangle(5, 5);
    Shape triangle = new Triangle(5, 5);

    IO.println("Circle Shape: " + circle.area());
    IO.println("Rectangle Shape: " + rectangle.area());
    IO.println("Triangle Shape: " + triangle.area());

    //LSP
    IO.println("\nLSP");
    Penguin penguin = new Penguin();
    Sparrow sparrow = new Sparrow();

    penguin.swim();
    penguin.eat();

    sparrow.eat();
    sparrow.fly();

    Shape rectangle2 = new Rectangle(5, 5);
    LSP.Question2.Shape square = new Square(9);


    System.out.println("Square Shape: " + square.area());
    IO.println("Rectangle Shape: " + rectangle2.area());

    //ISP
    System.out.println("\nISP");
    BasicPrinter basicPrinter = new BasicPrinter();
    basicPrinter.print();

    MultfunctionalPrinter multfunctionalPrinter = new MultfunctionalPrinter();
    multfunctionalPrinter.print();
    multfunctionalPrinter.fax();

    Airplane airplane = new Airplane();
    airplane.fly();

    Car car = new Car();
    car.drive();

    //DIP
    System.out.println("\nDIP");

    PaymentProcessor bkashPayment = new BkashPayment();
    PaymentProcessor nogodPayment = new NogodPayment();

    PaymentService bkashPay = new PaymentService(bkashPayment);
    PaymentService nogodPay = new PaymentService(nogodPayment);
    bkashPay.makePayment(500);
    System.out.println("\n");
    nogodPay.makePayment(500);

    System.out.println("\n");

    Notification emailNotification = new Notification(new EmailSender());
    Notification smsNotifcation = new Notification(new SmsSender());
    Notification pushNotifcation = new Notification(new PushNotifcationSender());


    emailNotification.notifyUser("Ami Email");
    smsNotifcation.notifyUser("Ami SMS");
    pushNotifcation.notifyUser("New msg come");

}
