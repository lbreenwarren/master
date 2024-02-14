import java.util.ArrayList;

public class Department {

    String depName;
    ArrayList<Course> courses = new ArrayList<>();

    public Department(String name) {
        this.depName = name;
    }


    public void add(Course addCourse) {
        courses.add(addCourse);
    }

    public void remove(Course removeCourse) {
        courses.remove(removeCourse);
    }

    public ArrayList<Course> getListing () {
        return courses;
    }

    public String toString() {
        String listing = depName + "\n";
        for (int i = 0; i<courses.size(); ++i){
            listing += courses.get(i) + "\n";
        }

        return listing;
    }

}

