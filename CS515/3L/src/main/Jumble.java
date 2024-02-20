import java.util.Scanner;
import charlist.Charlist;

/**
 * Reads input string and key, jumbling appropriately. 
 */
 public class Jumble {

    /**
     * Reads in a word & a key to jumble with, and prints jumbled word.
     * @param args command line arguments 
     *             (word to jumble followed by space separated jumble key)
     */
    public static void main(String[] args) {
        String str;
        Scanner scnr = new Scanner(System.in);
        int pos, len;

        System.out.println("Input word and key to jumble: ");

        while (scnr.hasNext()) {
            Charlist clist = new Charlist();

            str = scnr.next();
            len = str.length();

            System.out.print(str + " is jumbled by key ");

            for (int i = 0; i < len; i++) {
                pos = scnr.nextInt();
                if (pos % 2 == 1) { // odd number 
                    clist.insertFront(str.charAt(i));
                } else {
                    clist.insertRear(str.charAt(i));
                }
                System.out.print(pos);
            }

            System.out.println(" to " + clist);
            System.out.println("Input word and key to jumble: ");
        }
        System.out.print("\n");
        scnr.close();
    }

    // For testing purposes
    public static String jumble(String str, int[] key) {
        Charlist clist = new Charlist();
        int len = str.length();

        System.out.print(str + " is jumbled by key ");

        for (int i = 0; i < len; i++) {
            int pos = key[i];
            if (pos % 2 == 1) { // odd number 
                clist.insertFront(str.charAt(i));
            } else {
                clist.insertRear(str.charAt(i));
            }
            System.out.print(pos);
        }

        System.out.println(" to " + clist);
        return clist.toString();
    }

}