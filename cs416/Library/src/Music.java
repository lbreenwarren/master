import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Music extends Item {
    String title;
    String artist;
    String format;
    int year;
    ArrayList<String> tracks;

    public Music(String title, String artist, String format, int year, ArrayList<String> tracks) {
        this.title = title;
        this.artist = artist;
        this.format = format;
        this.year = year;
        this.tracks = tracks;
    }

    public String basicInfo() {

        String info = title + " by " + artist + " (" + format + ")";
       // if (isCheckedOut()) {
         //   info = info + " (checked out)";
        //}
        return info;
    }

    public String detailedInfo() {
        String tracklist = "";
        for (int i = 0; i < tracks.size(); ++i) {
            tracklist += "\n    " + tracks.get(i);
        }

        return "Title: " + title + "\nAvailable: " + !isCheckedOut()
                + "\nArtist: " + artist + "\nFormat: " + format
                + "\nYear of release: " + year + "\nTrack list: "
                + tracklist;

    }

    public void checkOut(LocalDate currDate) {
        setCheckedOut();
        setDueDate(currDate.plus(7, ChronoUnit.DAYS));
    }

    public boolean contains(String search) {
        for (int i = 0; i < tracks.size(); ++i) {
            if (tracks.get(i).toLowerCase().contains(search.toLowerCase())) {
                return true;
            }
        }
        return title.toLowerCase().contains(search.toLowerCase()) || artist.toLowerCase().contains(search.toLowerCase());
    }
}
