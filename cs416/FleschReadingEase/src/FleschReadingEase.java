import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FleschReadingEase {

    public static void main(String[] args) {

        String[] tokens = {""};
        int totalsyls = 0;
        try {
            tokens = getTokensFromFile("input.txt");
        } catch (Exception e) {
        }
        for (int i = 0; i < tokens.length; ++i) {
            totalsyls += getSyllableCount(tokens[i]);

        }
        System.out.println("syllables: " + totalsyls);
        System.out.println("sentances: " + getSentenceCount(tokens));
        System.out.println("words: " + tokens.length);
        System.out.println("score: " + getFleschScore(tokens));
    }

    public static String[] getTokensFromFile(String filename) throws FileNotFoundException {
        ArrayList<String> tokens = new ArrayList<>();
        File f = new File(filename);
        Scanner s = new Scanner(f);
        while (s.hasNext()){
            tokens.add(s.next());
        }
        return tokens.toArray(new String[0]);
    }

    public static String removeNonAlphabetCharacters(String text){
        String result = "";
        for(int i = 0; i<text.length(); ++i){
            if (Character.isLetter((text.charAt(i)))){
                result += text.charAt(i);
            }
        }
        return result;
    }

    public static int getSentenceCount(String[] tokens){
        int sentencecount = 0;
        for (int i = 0; i < tokens.length; ++i){

           for (int j = 0; j < tokens[i].length(); ++j){
               if (".!?;:".contains(Character.toString(tokens[i].charAt(j)))){
                   ++sentencecount;
                   break;
               }
           }
        }
        return sentencecount;
    }

    public static int getSyllableCount (String word) {

        int syllables = 0;
        int lastvowel = Integer.MAX_VALUE;

        String testword = removeNonAlphabetCharacters(word);

        for (int i = 0; i < testword.length(); ++i) {
            if ((i == testword.length() - 1)&&("eE".contains(Character.toString(testword.charAt(i))))) {}

            else{

                if(lastvowel == i - 1){
                    lastvowel = Integer.MAX_VALUE;
                }

                else {

                    if("aeiouyAEIOUY".contains(Character.toString(testword.charAt(i)))){
                        ++syllables;
                        lastvowel = i;
                    }
                }
            }
        }

        if (syllables == 0) {
            syllables = 1;
        }

        return syllables;

    }

    public static int getFleschScore (String[] text){
        int totalwords =  text.length;
        int totalsentences = getSentenceCount(text);
        int totalsyllables = 0;
        for (int i = 0; i < text.length; ++i) {
            totalsyllables += getSyllableCount(text[i]);
        }

        double fleschscore = 206.835 - (1.015 * ((double) totalwords/totalsentences)) - (84.6 * ((double) totalsyllables/totalwords));
        System.out.println("double fleschscore:" + fleschscore);
        return (int) Math.round(fleschscore);
    }
}
