public class Multiples implements Sequence {
    private int num;
    private int counter;

    public Multiples(int num){
        this.num = num;
        counter = 1;
    }
    public int nextVal() {
        int multiple = num * counter;
        ++counter;
        return multiple;
    }
    public void reset() {
        counter = 1;
    }

}
