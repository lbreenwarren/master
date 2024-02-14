public class Counter implements Freezable {
    int count;
    int increment;
    boolean frozen;

    public Counter(int count, int increment) {
        this.count = count;
        this.increment = increment;
        frozen = false;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        if (!frozen){
            count += increment;
        }
    }

    public void freeze(){
        frozen = true;
    }
    public void unfreeze(){
        frozen = false;
    }
}
