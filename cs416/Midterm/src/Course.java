public class Course {
    String name;
    int sectionNumber;

    public Course(String name, int sectionNumber) {
        this.name = name;
        this.sectionNumber = sectionNumber;

    }

    public String getName(){
        return name;
    }

    public int getSection() {
        return sectionNumber;
    }

    public String toString() {
        return name + " " + sectionNumber;
    }

}
