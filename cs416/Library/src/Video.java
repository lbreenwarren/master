import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Video extends Item {
    String title;
    String format;
    int year;
    int runtime;

    public Video(String title, String format, int year, int runtime) {
        this.title = title;
        this.format = format;
        this.year = year;
        this.runtime = runtime;
    }

    public String basicInfo() {

        String info = title + " (" + format + ")";
        //if (isCheckedOut()) {
          //  info = info + " (checked out)";
        //}

        return info;
    }

    public String detailedInfo() {
        return "Title: " + title + "\nAvailable: " + !isCheckedOut()
                + "\nFormat: " + format + "\nYear of release: "
                + year + "\nRuntime (minutes): " + runtime;
    }

    public void checkOut(LocalDate currDate) {
        setCheckedOut();
        setDueDate(currDate.plus(3, ChronoUnit.DAYS));
    }

    public boolean contains(String search) {
        return title.toLowerCase().contains(search.toLowerCase());
    }


}
