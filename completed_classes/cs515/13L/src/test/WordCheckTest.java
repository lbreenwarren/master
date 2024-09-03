import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WordCheckTest {
    static WordCheck wc;

    @BeforeAll
    public static void setUpWordCheck() throws Exception {
        System.out.println("Constructing WordCheck...");
        File file = new File("src/files/words");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        ArrayList<String> dict = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            dict.add(line);
        }
        wc = new WordCheck(dict.iterator());
        System.out.println("WordCheck Constructed");
    }

    @Test
    public void wine() {
        Set<String> res = wc.wordCheck("wine");
        String[] trueRes = { "nine", "wice", "pine", "line", "hine", "bine", "dine", "wise", "fine", "tine", "rine",
                "vine", "wime", "mine", "wins",
                "wide", "wino", "winn", "wife", "kine", "gine", "winy", "wyne", "wint", "wipe", "aine", "wene", "wire",
                "wina", "cine", "eine", "wite",
                "wane", "wive", "sine", "wone", "wink", "wini", "wile", "wing", "wind" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void nice() {
        Set<String> res = wc.wordCheck("nice");
        String[] trueRes = { "tice", "nine", "vice", "wice", "nace", "nike", "bice", "nile", "dice", "nife", "fice",
                "nica",
                "rice", "lice", "mice", "sice", "pice", "nide", "nick", "nich", "nico", "niue", "nixe" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void root() {
        Set<String> res = wc.wordCheck("root");
        String[] trueRes = { "rort", "poot", "roos", "toot", "ryot", "loot", "boot", "hoot", "roit", "foot", "roof",
                "rood", "riot", "rout", "roon", "rost", "roop", "rook", "royt", "room", "rowt", "rool", "moot", "soot",
                "coot" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);

    }

    @Test
    public void rose() {
        Set<String> res = wc.wordCheck("rose");
        String[] trueRes = { "rone", "role", "oose", "mose", "rope", "rode", "cose", "rosy", "rove", "rote", "roze",
                "nose", "ruse", "rome", "rtse", "roke", "pose", "jose", "lose", "roee", "ross", "hose", "fose", "rost",
                "rese", "bose", "dose", "rase", "robe", "roue", "rise", "roye", "rowe", "rosa" };

        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void pipe() {
        Set<String> res = wc.wordCheck("pipe");
        String[] trueRes = { "pope", "kipe", "pine", "hipe", "pile", "pipi", "pike", "pips", "pice", "wipe", "pize",
                "pape", "pipy", "pire", "ripe", "tipe", "yipe", "sipe", "xipe", "pepe", "pipa", "pise" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void head() {
        Set<String> res = wc.wordCheck("head");
        String[] trueRes = { "heat", "tead", "read", "hend", "held", "bead", "mead", "heid", "dead", "heed", "lead",
                "heaf", "heal", "hoad", "heao", "heap", "hear", "herd" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void shoe() {
        Set<String> res = wc.wordCheck("shoe");
        String[] trueRes = { "shee", "shoo", "shoq", "shop", "show", "shae", "shor", "shou", "shot", "shue", "shoa",
                "shog", "sloe", "shod" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void chat() {
        Set<String> res = wc.wordCheck("chat");
        String[] trueRes = { "that", "bhat", "phat", "chak", "khat", "chai", "ghat", "chad", "chae", "chab", "chac",
                "chut", "chaa", "coat", "clat", "what", "chay", "chaw", "char", "chas", "chet", "chap", "chan", "shat",
                "chao", "chit", "chal", "cham" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void your() {
        Set<String> res = wc.wordCheck("your");
        String[] trueRes = { "bour", "dour", "pour", "youp", "sour", "yous", "tour", "lour", "jour", "hour", "gour",
                "four", "youd" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void hand() {
        Set<String> res = wc.wordCheck("hand");
        String[] trueRes = { "hang", "hend", "yand", "hank", "hind", "hano", "hond", "hans", "hant", "rand", "pand",
                "land", "nand", "band", "hard", "hana", "dand", "fand", "gand", "haed", "hacd", "sand", "hund", "hynd",
                "wand", "mand", "cand", "kand", "iand" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void pear() {
        Set<String> res = wc.wordCheck("pear");
        String[] trueRes = { "perr", "year", "petr", "wear", "rear", "peas", "tear", "peal", "paar", "peer", "pean",
                "lear", "bear", "near", "dear", "peat", "jear", "phar", "hear", "fear", "peai", "peak", "peag", "sear",
                "mear", "cear", "gear" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void rest() {
        Set<String> res = wc.wordCheck("rest");
        String[] trueRes = { "rept", "sest", "rett", "best", "mest", "rent", "fest", "gest", "rust", "rect", "yest",
                "regt", "reet", "west", "pest", "test", "resp", "ress", "vest", "lest", "nest", "rost", "reit", "cest",
                "jest", "rist", "hest", "rese", "zest", "reft", "resa", "resh" };
        Set<String> trueResSet = new HashSet<>();
        for (String s : trueRes) {
            trueResSet.add(s);
        }
        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void you() {
        assertThrows(IllegalArgumentException.class, () -> wc.wordCheck("you"));
    }

    @Test
    public void money() {
        assertThrows(IllegalArgumentException.class, () -> wc.wordCheck("money"));
    }

}