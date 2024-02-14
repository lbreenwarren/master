import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class representing a library and providing an interactive user interface for
 * checking items in and out of the library.
 *
 * @author Sofia Lemons
 * @version 1.0
 */
public class Library {

    ArrayList<Item> catalog;
    Scanner input;
    int year;
    int month;
    int day;
    LocalDate currDate;
    String openClosed = "closed";
    ArrayList<Item> searchresults = new ArrayList<>();



    /**
     * Constructor for the Library class.
     *
     * @param catalog The catalog of all available items.
     * @param input The source of user input.
     * @param year The current year.
     * @param month The current month.
     * @param day The current day.
     */
    public Library(ArrayList<Item> catalog, Scanner input, int year, int month, int day) {
        //TODO: implement
        this.catalog = catalog;
        this.input = input;
        this.year = year;
        this.month = month;
        this.day = day;
        currDate = LocalDate.of(year, month, day);
    }

    /**
     * Starts the interactive library program.
     */
    public void start() {
        String start = "";
        while (!start.equals("o") && !(start.equals("O"))) {
            System.out.println("Enter o to open the library or x to exit the program.");
            start = input.nextLine();
            if (start.equals("x") || start.equals("X")) {
                return;
            }
        }
        if (start.equals("o") || start.equals("O")) {
            String monthStr = "" + month;
            String dayStr = "" + day;

            if (this.month < 10) {
                monthStr = "0" + month;
            }
            if (this.day < 10) {
                dayStr = "0" + day;
            }

            System.out.println("\nToday's date: " + year + "-" + monthStr + "-" + dayStr + "\n");
            System.out.println(open());
            System.out.println("\n\nEnter s to search for an item to check out/in or c to close the library.");
            String search = input.nextLine();
            while (search.equals("s") || search.equals("S")) {

                System.out.println("Enter search text.");
                //input.nextLine();
                String searchTerm = input.nextLine();
                search(searchTerm);
                ArrayList<Item> searchResult = getSearchResults();
                System.out.println("\nResults:");
                for (int i = 0; i < searchResult.size(); ++i) {
                    int k = i + 1;
                    String list = k + ": " + searchResult.get(i).basicInfo();
                    if (searchResult.get(i).isCheckedOut()){
                        list = list + " (checked out)";
                    }
                    System.out.println(k + ": " + searchResult.get(i).basicInfo());
                }
                System.out.println("\nEnter o to check out, i to check in or x to cancel");
                String checkOutIn = input.nextLine();
                if (checkOutIn.equals("x") || checkOutIn.equals("X")) {
                    continue;
                }
                if (checkOutIn.equals("i") || checkOutIn.equals("I")) {
                    System.out.println("\nItem number to check in:");
                    int i = input.nextInt();
                    System.out.println(searchResult.get(i - 1).detailedInfo());
                    System.out.println("\nDo you want to check in this item? (Enter y or n)");
                    input.nextLine();
                    String yesno = input.nextLine();
                    //System.out.println(yesno);
                    if (yesno.equals("y") || yesno.equals("Y")) {
                        searchresults.get(i).checkIn();
                    }
                }
                if (checkOutIn.equals("o") || checkOutIn.equals("O")) {
                    System.out.println("\nItem number to check out:");
                    int i = input.nextInt();
                    System.out.println("\n" + searchresults.get(i - 1).detailedInfo());
                    System.out.println("\nDo you want to check out this item? (Enter y or n)");
                    input.nextLine();
                    String noyes = input.nextLine();
                    if (noyes.equals("y") || noyes.equals("Y")) {
                        searchresults.get(i - 1).checkOut(getCurrDate());
                    }
                }
                System.out.println("\nEnter s to search for an item to check out/in or c to close the library.");
                search = input.nextLine();

            }
            if (search.equals("c") || search.equals("C")) {
                close();
                start();
            }
        }
    }

    /**
     * Opens the library for the day.
     *
     * @return String A string containing information about overdue items.
     */
    public String open() {

        if (openClosed.equals("open")) {
            return "";

        } else {
            ArrayList<Item> overdue = new ArrayList<>();


            for (int i = 0; i < catalog.size(); ++i) {

                if ((catalog.get(i).getDueDate() != null) && (catalog.get(i).getDueDate().isBefore(currDate))) {
                    overdue.add(catalog.get(i));
                }
            }

            String overdueinfo = "";
            for (int i = 0; i < overdue.size(); ++i) {

                overdueinfo = overdueinfo + "\n    " + overdue.get(i).basicInfo()
                        + " was due on " + overdue.get(i).getDueDate();
            }
            openClosed = "open";
            return "Overdue items:" + overdueinfo;
        }
    }

    /**
     * Closes the library for the day.
     */
    public void close() {
        if (openClosed.equals("closed")) {
            return;
        } else {
            openClosed = "closed";
            currDate = currDate.plus(1, ChronoUnit.DAYS);
            day = day + 1;
        }
    }

    /**
     * Perform a search for any items which contain the search text.
     *
     * @param search The search text.
     */
    public void search(String search) {

        searchresults.clear();

        if (openClosed.equals("closed")) {
            System.out.println("Library closed");
        } else {
            for (int i = 0; i < catalog.size(); ++i) {
                if (catalog.get(i).contains(search)) {
                    searchresults.add(catalog.get(i));
                }
            }
        }
    }

    /**
     *Check in an item from the current patron's checked out items.
     *
     * @param index The index (starting at 1) of the item.
     */
    public void checkIn(int index) {
        searchresults.get(index - 1).checkIn();
    }

    /**
     * Check out an item from the most recent search result list.
     *
     * @param index The index (starting at 1) of the item.
     */
    public void checkOut(int index) {
        searchresults.get(index - 1).checkOut(currDate);
    }

    /**
     * Get the current date.
     *
     * @return LocalDate The current date.
     */
    public LocalDate getCurrDate() {
        return currDate;
    }

    /**
     * Get the entire catalog of available items.
     *
     * @return The catalog.
     */
    public ArrayList<Item> getCatalog() {
        return catalog;
    }

    /**
     * Returns the results from the most recent search, or null if no results are currently stored.
     *
     * @return The most recent search results.
     */
    public ArrayList<Item> getSearchResults() {
        return searchresults;
    }

    /**
     * Indicates whether the library is open or not.
     *
     * @return boolean True if library is open, false otherwise.
     */
    public boolean isOpen() {
        return openClosed.equals("open");
    }

    /////////////////// DO NOT CHANGE THE CODE BELOW /////////////////////

    /**
     * Utility method for reading the entire library catalog from input.
     * Input file must give the type of item first (BOOK, MUSIC, or VIDEO.)
     * Includes a blank line after each item, and END to signify the end of the data.
     *
     * @param fileName Location of input file.
     * @return The catalog.
     */
    public static ArrayList<Item> readCatalog(String fileName) {

        Scanner input = null;
        try {
            input = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
            System.exit(-1);
        }

        String line = "";
        ArrayList<Item> catalog = new ArrayList<Item>();
        boolean stop = false;
        while (!stop) {
            line = input.nextLine();
            switch (line) {
                case "BOOK":
                    String title = input.nextLine();
                    String author = input.nextLine();
                    int pages = input.nextInt();
                    int year = input.nextInt();
                    catalog.add(new Book(title, author, pages, year));
                    input.nextLine();
                    break;
                case "MUSIC":
                    title = input.nextLine();
                    String artist = input.nextLine();
                    String format = input.nextLine();
                    year = input.nextInt();
                    input.nextLine();
                    ArrayList<String> tracks = new ArrayList<String>();
                    while (true) {
                        line = input.nextLine();
                        if (line.equals("")) {
                            break;
                        }
                        tracks.add(line);
                    }
                    catalog.add(new Music(title, artist, format, year, tracks));
                    break;
                case "VIDEO":
                    title = input.nextLine();
                    format = input.nextLine();
                    year = input.nextInt();
                    int runtime = input.nextInt();
                    catalog.add(new Video(title, format, year, runtime));
                    input.nextLine();
                    break;
                case "END":
                    stop = true;
                    break;
            }
        }

        return catalog;
    }

    /**
     * Main method for the library program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter catalog file name.");
        String fileName = input.nextLine();

        int year;
        int month;
        int day;
        System.out.println("Enter the current year.");
        year = input.nextInt();
        System.out.println("Enter the current month.");
        month = input.nextInt();
        System.out.println("Enter the current day.");
        day = input.nextInt();
        input.nextLine();

        ArrayList<Item> catalog = Library.readCatalog(fileName);

        Library l = new Library(catalog, input, year, month, day);

        l.start();
    }
}
