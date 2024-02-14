import java.util.ArrayList;

public class Department {
    
    ArrayList<Course> courseListing = new ArrayList<Course>();
    String name;

    public Department(String name) {
        this.name = name;
    }

    public void add (Course crs) {
        courseListing.add(crs);
    }

    public void remove (Course crs) {
        courseListing.remove(crs);
    }

    public ArrayList<Course> getListing() {
        return courseListing;
    }

    public String toString() {
        String ret = name + "\n";
        for (int i = 0; i < courseListing.size(); ++i) {
            ret += courseListing.get(i).toString() + "\n";
        }
        return ret;
    }
}
