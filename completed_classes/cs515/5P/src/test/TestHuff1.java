import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestHuff1 {

    @Test
    void exampleTest() {

        HuffTree h = new HuffTree();
        char[] c = new char[]{'e', 'f', 'd', 'a', 'g'};
        int[] f = new int[]{12, 4, 2, 3, 1};
        h.buildTree(c,f,5);

        System.out.println(h.getCode('e'));
        System.out.println(h.getCode('f'));
        System.out.println(h.getCode('a'));
        System.out.println(h.getCode('g'));
        System.out.println(h.getCode('d'));
    }
}
