import java.util.Scanner;
/**A program to calculate the reading score of a given text.
 * @author Liam Warren
 * @version 9.19.23
 */

public class ReadingScore {

    /** Remove punctuation from string.
     * @param token string to remove punctuation from
     * @return formated string
     */

    public static String formatToken(String token) {
        String str = "";

        for (int i = 0; i < token.length(); ++i) {

            if (Character.isLetter(token.charAt(i))) {
                str += token.charAt(i);
            }

        }

        return str;
    }

    /**
     * Converts string into an array of strings separated by space.
     * @param text string to tokenize
     * @return array of strings 
     */
    public static String[] tokenize(String text) {

        String[] tokens = text.split("\\s+");
        return tokens;

    }

    /**
     * calculates the number of sentances in the text.
     * @param tokens tokenized text
     * @return number of sentences
     */
    public static int getSentenceCount(String[] tokens) {

        int count = 0;
        String sentanceMarks = ".:;?!";

        for (int i = 0; i < tokens.length; ++i) {

            if (tokens[i].length() != 0) {

                //.contains doesnt take a char so we need make it a string
                String lastChar = "" + tokens[i].charAt(tokens[i].length() - 1);

                if (sentanceMarks.contains(lastChar)) {
                    ++count;
                }

            }
        }

        return count;
    }

    /**
     * gets number of tokens.
     * @param tokens tokenized text
     * @return number of tokens
     */
    public static int getTokenCount(String[] tokens) {
        return tokens.length;
    }

    /** Calculates number of syllables in a word.
     * 
     * @param word to calculate number of syllables in
     * @return number of syllables
    */
    public static int getSyllableCount(String word) {

        int count = 0;
        String vowels = "aeiouyAEIOUY";
        word = word.toUpperCase();
        String formattedWord = formatToken(word);

        for (int i = 0; i < formattedWord.length(); ++i) {

            //.contains doesnt take a char so we need make it a string
            String character = "" + formattedWord.charAt(i);
            
            if (vowels.contains(character)) {
                ++count;
            }
        }

        if (word.charAt(word.length() - 1) == 'E') {
            --count;
        }

        if (count == 0) {
            count = 1;
        }
        
        return count;
    }

    /**
     * calculates number of syllables in a text.
     * @param tokens tokenized text
     * @return number of syllables
     */
    public static double getTotalSyllableCount(String[] tokens) {
      
        double count = 0;
       
        for (int i = 0; i < tokens.length; ++i) {
            count += getSyllableCount(tokens[i]);
        }
       
        return count * .88;
    }

    /**
     * Calculate reading score.
     * @param text text to calculate score of
     * @return score
     */
    public static int getReadingScore(String text) {

        double scoreDouble = 0;
        String[] tokens = tokenize(text);
        double sentences = getSentenceCount(tokens);
        double totalSyllables = getTotalSyllableCount(tokens);
        double words = getTokenCount(tokens);
        scoreDouble  =  206.835 - (1.015 * (words / sentences)) - (84.6 * (totalSyllables / words));
        int score = (int) Math.round(scoreDouble);
        
        if (score > 100) {
            score = 100;
        }
        
        return score;



    }
    
    /**
     * main method.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
      
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        int score = getReadingScore(text);
        System.out.println(score);

        if (score <= 10) {
            System.out.println("Extremely difficult to read.");
        }
        if (score > 10 && score <= 30) {
            System.out.println("Very difficult to read.");
        }
        if (score > 30 && score <= 50) {
            System.out.println("Difficult to read.");
        }
        if (score > 50 && score <= 60) {
            System.out.println("Fairly difficult to read.");
        }
        if (score > 60 && score <= 70) {
            System.out.println("Plain English.");
        }
        if (score > 70 && score <= 80) {
            System.out.println("Fairly easy to read.");
        }
        if (score > 80 && score <= 90) {
            System.out.println("Easy to read.");
        }
        if (score > 90 && score <= 100) {
            System.out.println("Very easy to read.");
        }        

        in.close();
    }
}
