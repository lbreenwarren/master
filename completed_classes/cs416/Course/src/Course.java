public class Course {
    private String subject;
    private int number;

    public Course(String subject, int number) {

        if (number>799 || number < 401){
            number = 401;
        }
        if (subject.length() > 4){
            String buff = "";
            for (int i = 0; i<4; ++i){
                buff = buff + subject.charAt(i);
            }
            subject = buff;
        }

        this.subject = subject.toUpperCase();
        this.number = number;
    }

    public String toString(){
        return subject + " " + number;
    }

}


