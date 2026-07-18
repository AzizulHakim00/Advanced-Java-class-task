package ISP.Question1;

public class MultfunctionalPrinter implements Faxable , Printable, Scannable{
    @Override
    public void fax() {
        System.out.printf("Faxing");
    }

    @Override
    public void print() {
        System.out.printf("Pritinig");
    }

    @Override
    public void scan() {
        System.out.println("Scanning");
    }
}
