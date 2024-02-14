public class Course {

    String name;
    int sectionNum;

    public Course(String name, int sectionNum) {
        this.name = name;
        this.sectionNum = sectionNum;
    } 

    public String getName() {
        return name;
    }

    public int getSection() {
        return sectionNum;
    }

    public String toString() {
        return name + " " + sectionNum;
    }

}