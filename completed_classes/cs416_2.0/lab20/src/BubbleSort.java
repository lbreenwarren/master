/**A program to sort a list of samples using Bubble Sort.
 * @author Liam Warren
 * @version 11.27.23
 */
public class BubbleSort extends Sort {

    /**Constructor for BubbleSort.
     * @param filename data file
     */
    public BubbleSort(String filename) {
        
        super(filename);
    }

    /**goes through each index and swaps with next index if its greater than current. */
    public void sort() {

        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < samples.size() - 1; ++i) {
                if (samples.get(i).measurement < samples.get(i + 1).measurement) {
                    Sample sample1 = samples.get(i);
                    Sample sample2 = samples.get(i + 1);
                    samples.set(i, sample2);
                    samples.set(i + 1, sample1);
                    sorted = false;
                }       
            }
        }
    }
}
