import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * The DNA class contains methods for reading DNA data from files and finding
 * maximum consecutive matches in DNA sequences.
 * @author Liam Warren
 * @version 11.7.23
 */
public class DNA {

    /**
     * Reads DNA data from a file and returns an array of DNA records.
     *
     * @param fileName The name of the file to read DNA data from.
     * @return An array of DNA records, or null if the file is not found.
     */
    public static DNARecord[] readData(String fileName) {
        DNARecord[] record = new DNARecord[20];

        FileReader data;

        try {
            data = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }

        Scanner in = new Scanner(data);
        in.nextLine();
        int i = 0;
        while (in.hasNext()) {
            String name = in.next();
            Profile profile = new Profile();
            profile.setAgatCount(in.nextInt());
            profile.setAatgCount(in.nextInt());
            profile.setTatcCount(in.nextInt());

            record[i] = new DNARecord(name, profile);
            ++i;
        }
        
        in.close();
        return record;
    }

    /**
     * Counts the maximum number of consecutive matches of a given sequence in
     * another sequence.
     *
     * @param sequence The sequence to search in.
     * @param match    The sequence to find and count matches for.
     * @return The maximum number of consecutive matches of the 'match' sequence
     *         found in the 'sequence'.
     */
    public static int countMaximumConsecutiveMatches(String sequence, String match) {
        char[] sequenceChars = sequence.toCharArray();
        char[] matchChars = match.toCharArray();

        int i = 0;
        int maximumMatches = 0;
        int matches = 0;
        int offset = 0;

        while (i + offset < sequenceChars.length) {
            if (sequenceChars[i + offset] == matchChars[offset]) {
                if (offset == matchChars.length - 1) {
                    ++matches;
                    if (matches > maximumMatches) {
                        maximumMatches = matches;
                    }
                    offset = 0;
                    i += matchChars.length;
                } else {
                    ++offset;
                }
            } else {
                matches = 0;
                ++i;
                offset = 0;
            }
        }

        return maximumMatches;
    }

    /**
     * Reads a DNA sequence from a file and returns a profile containing the counts
     * of AGAT, AATG, and TATC repeats.
     *
     * @param fileName The name of the file containing the DNA sequence.
     * @return A Profile object with counts of AGAT, AATG, and TATC repeats.
     */
    public static Profile readSequence(String fileName) {
        Profile profile = new Profile();

        FileReader data;

        try {
            data = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }

        Scanner in = new Scanner(data);
        String sequence = in.nextLine();

        profile.setAgatCount(countMaximumConsecutiveMatches(sequence, "AGAT"));
        profile.setAatgCount(countMaximumConsecutiveMatches(sequence, "AATG"));
        profile.setTatcCount(countMaximumConsecutiveMatches(sequence, "TATC"));

        in.close();

        return profile;
    }

    /** Main method to parse data record and sequence files and indicate a match.
    * @param  args filenames for data file and sequence file
    */
    public static void main(String[] args) {
        String dataFile = args[0];
        String sequenceFile = args[1];
        DNARecord[] record = readData(dataFile);
        Profile prof = readSequence(sequenceFile);
        int i = 0;
        boolean found = false;
        while (i < 20 && record[i] != null) {
            
            if (record[i].getProfile().equals(prof)) {
                System.out.println("Matched " + record[i]);
                found = true;
            }
            ++i;
        }
        if (!found) {
            System.out.println("No match for " + prof);
        }
    }
}
