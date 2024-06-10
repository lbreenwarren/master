import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WordCheck {
    // You are allowed to add private methods, or private members but you cannot
    // alter the return or parameter type of either the following public
    // methods.
    final static String alphabet = "abcdefghijklmnopqrstuvwxyz";
    Map<Character, Integer> alphabetIndex;
    Trie trie;
    /**
     * @param dictionary
     */
    public WordCheck(Iterator<String> dictionary) {
        // TODO : Setup the wordcheck class given the dictionary
        // This should only happen once at construction, during the wordCheck
        // function call the ArrayList dictionary should not be needed.
        // Feel free to add a private class member which organizes the
        // information in the dictionary for later queries.
        trie = new Trie();
        alphabetIndex = new HashMap<Character, Integer>();
        char[] alphaChars = alphabet.toCharArray();
        for (int i = 0; i < alphaChars.length; ++i) {
            alphabetIndex.put(alphaChars[i], i);
        }
        while (dictionary.hasNext()) {
            String word = dictionary.next();
            word = word.toLowerCase();
            trie.insert(word);
        }

    }

    /**
     * @param query
     * @return Set<alteredWords>
     */
    public Set<String> wordCheck(String query) throws IllegalArgumentException{
        if (query.length() > 4 || query.length() < 4) {
            throw new IllegalArgumentException();
        }
        char[] queryChars = query.toCharArray();
        Set<String> ret = new HashSet<String>();
        char[] alphaChars = alphabet.toCharArray();

        for (int i = 0; i < queryChars.length; ++i) {

            for (int k = 0; k < alphabetIndex.size(); ++k) {

                if (k != alphabetIndex.get(queryChars[i])) {

                    char[] altString = new char[queryChars.length];
                    altString = queryChars.clone();
                    altString[i] = alphaChars[k];
                    String alt = String.copyValueOf(altString);
                    if (trie.contains(alt)) {
                        ret.add(alt);
                    }
                }
            }
        }
        return ret;
    }
}
