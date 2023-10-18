public class Counter implements Freezable{
    
    int count;
    int increment;
    boolean frozen;
    
    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    public Counter (int count, int increment) {
        frozen = false;
        this.count = count;
        this.increment = increment;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        if (!frozen) {
            count += increment;
        }
    }
}
