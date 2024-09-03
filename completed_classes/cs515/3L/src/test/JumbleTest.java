import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JumbleTest {
    @Test 
    public void test_jumble_hello_1111() {
        String str = "word";
        int[] key = new int[] {1, 1, 1, 1};
        assertEquals(Jumble.jumble(str, key), "drow");
    }

    @Test
    public void test_jumble_hello_1110() {
        String str = "word";
        int[] key = new int[] {1, 1, 1, 0};
        assertEquals(Jumble.jumble(str, key), "rowd");
    }

    @Test 
    public void test_jumble_hello_1100() {
        String str = "word";
        int[] key = new int[] {1, 1, 0, 0};
        assertEquals(Jumble.jumble(str, key), "owrd");
    }

    @Test 
    public void test_jumble_hello_1000() {
        String str = "word";
        int[] key = new int[] {1, 0, 0, 0};
        assertEquals(Jumble.jumble(str, key), "word");
    }

    @Test
    public void test_jumble_hello_0011() {
        String str = "word";
        int[] key = new int[] {0, 0, 1, 1};
        assertEquals(Jumble.jumble(str, key), "drwo");
    }

    @Test
    public void test_jumble_hello_0101() {
        String str = "word";
        int[] key = new int[] {0, 1, 0, 1};
        assertEquals(Jumble.jumble(str, key), "dowr");
    }

    @Test
    public void test_jumble_hello_0000() {
        String str = "word";
        int[] key = new int[] {0, 0, 0, 0};
        assertEquals(Jumble.jumble(str, key), "word");
    }

    @Test
    public void test_jumble_string_111111() {
        String str = "string";
        int[] key = new int[] {1, 1, 1, 1, 1, 1};
        assertEquals(Jumble.jumble(str, key), "gnirts");
    }

    @Test
    public void test_jumble_string_111011() {
        String str = "string";
        int[] key = new int[] {1, 1, 1, 0, 1, 1};
        assertEquals(Jumble.jumble(str, key), "gnrtsi");
    }

    @Test
    public void test_jumble_string_110011() {
        String str = "string";
        int[] key = new int[] {1, 1, 0, 0, 1, 1};
        assertEquals(Jumble.jumble(str, key), "gntsri");
    }

    @Test
    public void test_jumble_string_101011() {
        String str = "string";
        int[] key = new int[] {1, 0, 1, 0, 1, 1};
        assertEquals(Jumble.jumble(str, key), "gnrsti");
    }

    @Test
    public void test_jumble_string_110001() {
        String str = "string";
        int[] key = new int[] {1, 1, 0, 0, 0, 1};
        assertEquals(Jumble.jumble(str, key), "gtsrin");
    }
    
    @Test
    public void test_jumble_string_100011() {
        String str = "string";
        int[] key = new int[] {1, 0, 0, 0, 1, 1};
        assertEquals(Jumble.jumble(str, key), "gnstri");
    }

    @Test
    public void test_jumble_string_101101() {
        String str = "string";
        int[] key = new int[] {1, 0, 1, 1, 0, 1};
        assertEquals(Jumble.jumble(str, key), "girstn");
    }

    @Test
    public void test_jumble_string_000011() {
        String str = "string";
        int[] key = new int[] {0, 0, 0, 0, 1, 1};
        assertEquals(Jumble.jumble(str, key), "gnstri");
    }

    @Test
    public void test_jumble_string_000000() {
        String str = "string";
        int[] key = new int[] {0, 0, 0, 0, 0, 0};
        assertEquals(Jumble.jumble(str, key), "string");
    }
    
}
