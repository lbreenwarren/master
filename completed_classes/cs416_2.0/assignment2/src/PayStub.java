import java.util.Scanner;

/**
 * The PayStub class represents a pay stub for an employee.
 * @author Liam Warren
 * @version 9.28.23
 */
public class PayStub {

    // Fields to store employee information
    String name;
    int anniversaryMonth;
    int anniversaryYear;
    int hours;
    String title;
    double payRate;
    double vacationHours;
    double grossPay;
    double retHold;
    double tax;
    double netPay;

    /**
     * Constructs a PayStub object with the given employee information.
     *
     * @param name            The employee's name.
     * @param anniversaryMonth The anniversary month of the employee.
     * @param anniversaryYear  The anniversary year of the employee.
     * @param hours           The number of hours worked.
     * @param title           The job title of the employee.
     * @param payRate         The pay rate of the employee.
     */
    public PayStub(String name, int anniversaryMonth, int anniversaryYear, int hours, String title, double payRate) {
        this.name = name;
        this.anniversaryMonth = anniversaryMonth;
        this.anniversaryYear = anniversaryYear;
        this.hours = hours;
        this.title = title;
        this.payRate = payRate;
        this.vacationHours = vacationHours();
        this.grossPay = grossPay();
        this.retHold = retHold();
        this.tax = tax();
        this.netPay = netPay();
    }

    /**
     * Gets the name of the employee.
     *
     * @return The name of the employee.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the anniversary month of the employee.
     *
     * @return The anniversary month of the employee.
     */
    public int getMonth() {
        return anniversaryMonth;
    }

    /**
     * Gets the anniversary year of the employee.
     *
     * @return The anniversary year of the employee.
     */
    public int getYear() {
        return anniversaryYear;
    }

    /**
     * Gets the number of hours worked by the employee.
     *
     * @return The number of hours worked.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Gets the job title of the employee.
     *
     * @return The job title of the employee.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Calculates the number of months worked by the employee.
     *
     * @return The number of months worked.
     */
    public int numMonthsWorked() {
        int years = 2019 - anniversaryYear;
        int months = (years * 12) - anniversaryMonth + 1;
        return months;
    }

    /**
     * Calculates the vacation hours based on the number of months worked.
     *
     * @return The vacation hours earned.
     */
    public double vacationHours() {
        int monthsworked = numMonthsWorked();
        double vacationTime = monthsworked * 8.25;
        return vacationTime;
    }

    /**
     * Calculates the gross pay for the employee.
     *
     * @return The gross pay.
     */
    public double grossPay() {
        return hours * payRate;
    }

    /**
     * Calculates the retirement hold for the employee.
     *
     * @return The retirement hold amount.
     */
    public double retHold() {
        return grossPay() * .052;
    }

    /**
     * Calculates the tax amount for the employee.
     *
     * @return The tax amount.
     */
    public double tax() {
        return (grossPay() - retHold()) * .28;
    }

    /**
     * Calculates the net pay for the employee.
     *
     * @return The net pay.
     */
    public double netPay() {
        return grossPay() - tax();
    }

    /**
     * Prints the company logo.
     */
    public void drawLogo() {
        System.out.println("      Gekko & Co.\n");
        System.out.println("          \"$\"");
        System.out.println("          ~~~");
        System.out.println("         /  \\ `.");
        System.out.println("        /    \\  /");
        System.out.println("       /_ _ _ \\/");
    }

    /**
     * Prints the employee's pay stub information.
     */
    public void printInfo() {
        System.out.println("==========================================");
        drawLogo();
        System.out.println("\n------------------------------------------");
        System.out.println("Pay Period:     1/2019");
        System.out.println("Name:           " + name);
        System.out.println("Title:          " + title);
        System.out.println("Anniversary:    " + anniversaryMonth + "/" + anniversaryYear);
        System.out.println("Months Worked:  " + numMonthsWorked() + " months");
        System.out.printf("Vacation hours: %.2f\n", vacationHours);
        System.out.println("------------------------------------------");
        System.out.printf("Gross Pay:   $%7.2f\n", grossPay);
        System.out.printf("Retirement:  $%7.2f\n", retHold);
        System.out.printf("Tax:         $%7.2f\n", tax);
        System.out.println("------------------------");
        System.out.printf("Net Pay:     $%7.2f\n", netPay);
        System.out.println("==========================================");
    }

    /**
     * The main method for running the PayStub program.
     *
     * @param args Command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter your Fullname: ");
        String name = in.nextLine();
        System.out.print("Enter your Anniversary Month(1-12): ");
        int month = in.nextInt();
        System.out.print("Enter your Anniversary Year: ");
        int year = in.nextInt();
        System.out.print("Enter your hours worked this pay period(0-350): ");
        int hours = in.nextInt();
        System.out.print("Enter your Job Title: ");
        in.nextLine();
        String title = in.nextLine();
        System.out.print("Enter your pay rate: ");
        double payRate = in.nextDouble();
        PayStub stub = new PayStub(name, month, year, hours, title, payRate);
        stub.printInfo();
    }
}
