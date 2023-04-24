import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Book extends Item {
    String title;
    String author;
    int pages;
    int year;

    public Book(String title, String author, int pages, int year) {
        this.title = title;
        this.author = author;
        this.pages =  pages;
        this.year = year;
    }

    public String basicInfo() {
        String info = title + ", by " + author;
        if (isCheckedOut()) {
            info = info + " (checked out)";
        }

        return info;
    }

    public String detailedInfo() {
        return "Title: " + title + "\nAvailable: " + !isCheckedOut()
                + "\nAuthor: " + author + "\nPages: " + pages
                + "\nPublication year: " + year;
    }

    public void checkOut(LocalDate currDate) {
        setCheckedOut();
        setDueDate(currDate.plus(14, ChronoUnit.DAYS));
    }

    public boolean contains(String search) {
        return title.toLowerCase().contains(search.toLowerCase()) || author.toLowerCase().contains(search.toLowerCase());
    }


}
