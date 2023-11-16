public class Multiples implements Sequence{

    private int num;
    private int counter;
    
    public Multiples(int num) {
        this.num = num;
        counter = 0;
    }

    public int nextVal() {
        ++counter;
        int value = 0;
        
        for (int i = 1; i <= counter; ++i) {
            value += num;
        }
        
        return value;
    }

    public void reset() {
        counter = 0;
    }

    // YOUR CODE HERE

}
