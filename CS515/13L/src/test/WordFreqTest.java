import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;;

public class WordFreqTest {
    static WordFreq wf;

    @BeforeAll
    public static void wordFreqSetup() throws IOException {
        System.out.println("Constructing WordFreq...");
        CSVReader reader = new CSVReaderBuilder(new FileReader("src/files/select_words.csv")).build();
        String[] nextLine;
        ArrayList<Triplet<String, Integer, Integer>> dict = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            String word = nextLine[0];
            int year = Integer.parseInt(nextLine[1]);
            int freq = Integer.parseInt(nextLine[2]);
            dict.add(new Triplet<>(word, year, freq));
        }
        wf = new WordFreq(dict.iterator());
        System.out.println("WordFreq Constructed");
    }

    @Test
    public void queer1995() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("queer", 1995);
        int[][] trueRes = { { 1995, 27142 }, { 1996, 28145 }, { 1997, 37137 }, { 1998, 36397 }, { 1999, 36684 },
                { 2000, 47629 }, { 2001, 47251 }, { 2002, 48482 }, { 2003, 48689 }, { 2004, 76794 }, { 2005, 71632 },
                { 2006, 83210 }, { 2007, 119333 }, { 2008, 180956 } };
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();
        for (int[] s : trueRes) {
            trueResSet.add(new Pair(s[0], s[1]));
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void advised1999() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("advised", 1999);
        int[][] trueRes = { { 1999, 138301 }, { 2000, 155644 }, { 2001, 159699 }, { 2002, 173951 }, { 2003, 188952 },
                { 2004, 212502 }, { 2005, 200794 }, { 2006, 215711 }, { 2007, 227259 }, { 2008, 287491 } };
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();
        for (int[] s : trueRes) {
            trueResSet.add(new Pair(s[0], s[1]));
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void quit2000() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("quit", 2000);
        int[][] trueRes = { { 2000, 112407 }, { 2001, 115302 }, { 2002, 130463 }, { 2003, 143924 }, { 2004, 157639 },
                { 2005, 155246 }, { 2006, 160284 }, { 2007, 172711 }, { 2008, 227876 } };
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();
        for (int[] s : trueRes) {
            trueResSet.add(new Pair(s[0], s[1]));
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void airport1900() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("airport", 1900);
        int[][] trueRes = { { 1900, 443 }, { 1901, 5 }, { 1902, 7 }, { 1903, 17 }, { 1904, 4 }, { 1905, 15 },
                { 1906, 55 }, { 1907, 1 }, { 1908, 1 }, { 1909, 1 }, { 1910, 7 }, { 1911, 3 }, { 1912, 152 },
                { 1913, 6 }, { 1914, 20 }, { 1915, 1 }, { 1916, 8 }, { 1917, 7 }, { 1918, 6 }, { 1919, 7 }, { 1920, 9 },
                { 1921, 2 }, { 1922, 19 }, { 1923, 23 }, { 1924, 45 }, { 1925, 39 }, { 1926, 170 }, { 1927, 1191 },
                { 1928, 657 }, { 1929, 3468 }, { 1930, 5867 }, { 1931, 3497 }, { 1932, 981 }, { 1933, 1283 },
                { 1934, 1081 }, { 1935, 1539 }, { 1936, 1525 }, { 1937, 2465 }, { 1938, 2686 }, { 1939, 3377 },
                { 1940, 8868 }, { 1941, 4709 }, { 1942, 7707 }, { 1943, 7122 }, { 1944, 6821 }, { 1945, 9597 },
                { 1946, 15933 }, { 1947, 9969 }, { 1948, 7864 }, { 1949, 10579 }, { 1950, 6974 }, { 1951, 7359 },
                { 1952, 5472 }, { 1953, 6440 }, { 1954, 7996 }, { 1955, 8997 }, { 1956, 7449 }, { 1957, 7901 },
                { 1958, 9254 }, { 1959, 10419 }, { 1960, 12261 }, { 1961, 11860 }, { 1962, 16793 }, { 1963, 14554 },
                { 1964, 15195 }, { 1965, 18997 }, { 1966, 19622 }, { 1967, 22495 }, { 1968, 24381 }, { 1969, 24256 },
                { 1970, 25204 }, { 1971, 27274 }, { 1972, 30449 }, { 1973, 29440 }, { 1974, 28823 }, { 1975, 31994 },
                { 1976, 28598 }, { 1977, 33827 }, { 1978, 32241 }, { 1979, 46200 }, { 1980, 37518 }, { 1981, 35716 },
                { 1982, 41397 }, { 1983, 45116 }, { 1984, 50011 }, { 1985, 49424 }, { 1986, 53547 }, { 1987, 58046 },
                { 1988, 60303 }, { 1989, 67752 }, { 1990, 76600 }, { 1991, 81484 }, { 1992, 84860 }, { 1993, 79187 },
                { 1994, 90668 }, { 1995, 87574 }, { 1996, 102357 }, { 1997, 103090 }, { 1998, 102422 },
                { 1999, 113734 }, { 2000, 140285 }, { 2001, 145810 }, { 2002, 163874 }, { 2003, 171735 },
                { 2004, 191086 }, { 2005, 173138 }, { 2006, 167451 }, { 2007, 175702 }, { 2008, 173294 } };
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();
        for (int[] s : trueRes) {
            trueResSet.add(new Pair(s[0], s[1]));
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void computer1900() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("computer", 1900);
        int[][] trueRes = {};
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void algorithm1900() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("algorithm", 1900);
        int[][] trueRes = {};
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void horse1900() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("horse", 1900);
        int[][] trueRes = {};
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

    @Test
    public void query1990() {
        Set<Pair<Integer, Integer>> res = wf.wordFreq("query", 1990);
        int[][] trueRes = { { 1990, 64481 }, { 1991, 57904 }, { 1992, 62268 }, { 1993, 63271 }, { 1994, 82676 },
                { 1995, 84469 }, { 1996, 81446 }, { 1997, 114311 }, { 1998, 112234 }, { 1999, 158019 },
                { 2000, 149893 }, { 2001, 180356 }, { 2002, 225454 }, { 2003, 251655 }, { 2004, 236153 },
                { 2005, 239560 }, { 2006, 238211 }, { 2007, 231507 }, { 2008, 223103 } };
        SortedSet<Pair<Integer, Integer>> trueResSet = new TreeSet<>();
        for (int[] s : trueRes) {
            trueResSet.add(new Pair(s[0], s[1]));
        }

        assertEquals(trueRes.length, res.size());
        assertEquals(trueResSet, res);
    }

}
