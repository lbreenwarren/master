/**A program to sort a list of samples using Insertion Sort.
 * @author Liam Warren
 * @version 11.27.23
 */
public class InsertionSort extends Sort {

    /**
     * Constructor for Insertion sort.
     * @param filename data file
     */
    public InsertionSort(String filename) {
        super(filename);
    }

    /**
     * Helper method to find and swap max remaining value to start index.
     * @param start index to swap to
     */
    public void findMaxandSwap(int start) {
        int minIndex = start;
        for (int i = start; i < samples.size(); ++i) {
            if (samples.get(i).measurement > samples.get(minIndex).measurement) {
                minIndex = i;
            }
        }
        Sample tmp = samples.get(start);
        samples.set(start, samples.get(minIndex));
        samples.set(minIndex, tmp);

    }

    /**
     * Goes through list and calls findMaxandSwap on each index.
     */
    public void sort() {
        for (int i = 0; i < samples.size(); ++i) {
            findMaxandSwap(i);
        }
    }
}
