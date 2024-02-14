import java.util.Random;

public class ListApp {

    static LinkedList generateData(int numKeys, int numItems, int seed) {
        LinkedList l = new LinkedList();
        Random rng;
        if (seed < 0) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }

        String letters = "abcdefghijkloaeipr";
        String[] keys = new String[ numKeys ];

        String uniqueKeys = "";
        for (int i = 0; i < numKeys; i++) {
            int letter1 = rng.nextInt(letters.length());
            int letter2 = rng.nextInt(letters.length());
            keys[ i ] =   letters.substring(letter1, letter1 + 1)
                    + letters.substring(letter2, letter2 + 1);
            uniqueKeys += keys[ i ]  + " ";
        }
        System.err.println(keys.length + "  unique keys: " + uniqueKeys);

        for (int i = 0; i < numItems; i++) {
            String key = keys[ rng.nextInt(keys.length)];
            int val = rng.nextInt(100);
            l.addHead(key, val);
        }
        return l;

    }

    public static void main(String[] args) {
        LinkedList list;
        list = ListApp.generateData( 5, 9, 1 );
        list.sort();
        System.out.println("List: " + list);
    }
}