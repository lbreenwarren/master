/**
 * A program to search an array using the comparable interface.
 * @author Liam Warren
 * @version 11.21.23
 * @param <T> generic Comparable type
 */
public class ArraySearch <T extends Comparable<? super T>> implements Search {
    
    Comparable[] list;

    /**
     * Constructor for ArraySearch.
     * @param list initialize list
     */
    public ArraySearch(T[] list) {
        this.list = list;
    }

    /**
     *
     * @return true if there are no element in the list.
     */
    public boolean isEmpty() {
        return list.length == 0;
    }

    /**
     *
     * @return the number of elements in the list
     */
    public int size() {
        return list.length;
    }

    /**
     *
     * @return true if the list is sorted in ascending order
     * For example: 1, 2, 3, 7, 20
     */
    public boolean isSorted() {
        if (size() <= 1) {
            return true;
        }
        for (int i = 0; i < size() - 1; ++i) {
            if (list[i].compareTo(list[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param obj object to be searched for
     * @return the location of the element in the list starting at 0 or return -1 if not present
     */
    public int index(Object obj) {

        for (int i = 0; i < size(); ++i) {
            if (list[i].compareTo(obj) == 0) {
                return i;
            }
        }
        
        return -1;
    }

}
