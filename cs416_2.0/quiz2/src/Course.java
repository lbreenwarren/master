
public class Course {

    private String subject;
    private int number;

    public Course(String name, int num) {
        subject = "";
        if (name.length() > 4) {
            for (int i = 0; i < 4; ++i) {
                subject += name.charAt(i);
            }
        } else {
            subject = name.toUpperCase();
        }

        if (num < 400 || num > 799) {
            number = 401;
        } else {
            number = num;
        }
    }

    public String toString(){
    
        return subject + " " + number;
    
    }

}

