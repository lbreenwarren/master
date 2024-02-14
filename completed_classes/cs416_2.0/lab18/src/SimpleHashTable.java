/**A program to generate hash values from keys.
 * @author Liam Warren
 * @version 10.16.23
 */
public class SimpleHashTable {
    
    String[] values = new String[10];
    
    /**
     * Adds value to array using key.
     * @param key key to calculate hash from
     * @param value String to be stored in array
     */
    public void put(int key, String value) {
        int hashIndex = hash(key, 10, 2);
        values[hashIndex] = value;
    }

    /**
     * Gets value from array using key.
     * @param key hash seed of index to grab String from
     * @return String at calculated index
     */
    public String get(int key) {
        int hashIndex = hash(key, 10, 2);
        return values[hashIndex];
    }

    /**
     * Prints a list of values stored in array with array index.
     * @return to String
     */
    public String toString() {
        String ret = "";
        for (int i = 0; i < values.length; ++i) {
            ret += i + " " + values[i] + "\n";
        }
        return ret;
    }

    /**
     * Hash using midsquare algorithm returns calculated array index.
     * @param key seed for hash calculation
     * @param size number of buckets
     * @param r number of digits in hash value
     * @return hash value
     */
    public static int hash(int key, int size, int r) {
        int squaredKey = key * key;
        String squaredKeyStr =  Integer.toString(squaredKey);
        int midIndex = (squaredKeyStr.length()) / 2;
        if (squaredKeyStr.length() % 2 == 0) {
            midIndex -= 1;
        }
        String midDigits = squaredKeyStr.substring(midIndex, midIndex + r);
        int hashValue = Integer.parseInt(midDigits);
        int hashIndex = hashValue % size;
        return hashIndex;


    }
}