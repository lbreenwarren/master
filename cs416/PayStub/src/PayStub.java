import java.util.Scanner;
/**A program to calculate vacation hours and net pay for an employee.
 * @author Liam Warren
 * @version 2/19/2023*/

public class PayStub {
    String name;
    int anniversaryMonth;
    int anniversaryYear;
    int hours;
    String title;
    double payRate;
    int monthsWorked;
    double vacationHours;
    double grossPay;
    double retirementWitholding;
    double taxWitholding;
    double netPay;

    /**Default constructor to initialize data members.*/
    PayStub() {
        monthsWorked = 0;
        vacationHours = 0;
        grossPay = 0;
        retirementWitholding = 0;
        taxWitholding = 0;
        netPay = 0;
    }

    /** Paramaterized constructor to intialize data members.
     * @param name employee name
     * @param anniversaryMonth month of starting
     * @param anniversaryYear year of starting
     * @param hours hours worked
     * @param title job title
     * @param payRate pay rate
     * */
    PayStub(String name, int anniversaryMonth, int anniversaryYear, int hours, String title, double payRate) {
        this.name = name;
        this.anniversaryMonth = anniversaryMonth;
        this.anniversaryYear = anniversaryYear;
        this.hours = hours;
        this.title = title;
        this.payRate = payRate;
    }

    /**getter for name.
     * @return name*/
    String getName() {
        return name;
    }

    /**getter for anniversary month.
     * @return anniversary month*/
    int getMonth() {
        return anniversaryMonth;
    }

    /**getter for anniversary year.
     * @return anniversaryYear*/
    int getYear() {
        return anniversaryYear;
    }

    /**getter for hours.
     * @return hours*/
    int getHours() {
        return hours;
    }

    /**getter for title.
     * @return title*/
    String getTitle() {
        return title;
    }

    /**getter for payRate.
     * @return payRate*/
    double getPayRate() {
        return payRate;
    }

    /**calculate total months worked.
     * @return monthsWorked*/
    int numMonthsWorked() {
        monthsWorked = (2018 - getYear()) * 12 + (13 - getMonth());
        return monthsWorked;
    }

    /**Calculate vacation hours.
     * @return vacationHours*/
    double vacationHours() {
        vacationHours = monthsWorked * 8.25;
        return vacationHours;
    }

    /**Calculate gross pay.
     * @return grossPay*/
    double grossPay() {
        grossPay = getHours() * getPayRate();
        return grossPay;
    }

    /**calculate retirement witholding.
     * @return retirementWitholding*/
    double retHold() {
        retirementWitholding = grossPay * .052;
        return retirementWitholding;
    }

    /**calculate tax witholding.
     * @return taxWitholding*/
    double tax() {
        taxWitholding = (grossPay - retirementWitholding) * .28;
        return taxWitholding;
    }

    /**calculate net pay.
     * @return netPay*/
    double netPay() {
        netPay = grossPay - retirementWitholding - taxWitholding;
        return netPay;
    }

    /**print company logo.*/
    void drawLogo() {
        System.out.println("      Gekko & Co.\n");
        System.out.println("          \"$\"");
        System.out.println("          ~~~");
        System.out.println("         /  \\ `.");
        System.out.println("        /    \\   /");
        System.out.println("       /_ _ _ \\/");

    }

    /**Print class info.*/
    void printInfo() {
        System.out.println("==========================================");
        drawLogo();
        System.out.println("\n------------------------------------------");
        System.out.println("Pay Period:     1/2019");
        name = getName();
        System.out.printf("Name:           %s\n", name);
        title = getTitle();
        System.out.printf("Title:          %s\n", title);
        int annmonth = getMonth();
        int annyear = getYear();
        System.out.printf("Anniversary:    %d/%d\n", annmonth, annyear);
        int monthsworked = numMonthsWorked();
        System.out.printf("Months Worked:  %d months\n", monthsworked);
        double vacay = vacationHours();
        System.out.printf("Vacation hours: %.2f\n", vacay);
        System.out.println("------------------------------------------");
        double grosspay = grossPay();
        System.out.printf("Gross Pay:     $%7.2f\n", grosspay);
        double retire = retHold();
        System.out.printf("Retirement:    $%7.2f\n", retire);
        double tax = tax();
        System.out.printf("Tax:           $%7.2f\n", tax);
        System.out.println("------------------------");
        double netpay = netPay();
        System.out.printf("Net Pay:       $%7.2f\n", netpay);
        System.out.println("==========================================");


    }

    /**main method, takes input from user.
     * @param args no command arguments used*/
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter your Fullname: ");
        String name = in.nextLine();
        System.out.print("Enter your Anniversary Month(1-12): ");
        int annmonth = in.nextInt();
        System.out.print("Enter your Anniversary Year: ");
        int annyear = in.nextInt();
        System.out.print("Enter your hours worked this pay period(0-350): ");
        int hours = in.nextInt();
        in.nextLine();
        System.out.print("Enter your Job Title: ");
        String title = in.nextLine();
        System.out.print("Enter your pay rate: ");
        double pay = in.nextDouble();
        PayStub c = new PayStub(name, annmonth, annyear, hours, title, pay);
        c.printInfo();



    }
}
