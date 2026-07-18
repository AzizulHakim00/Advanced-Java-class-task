package SRP.Question1;

public class ReportGenerator {

    public void generateReport(Employee employee, double totalSalary) {
        IO.println("Employee Name: " + employee.getName());
        IO.println("Employee ID: " + employee.getId());
        IO.println("Salary: " + totalSalary);
    }
}
