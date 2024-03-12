import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

public class HuffCoder {
    int originalBits;
    int compressedBits;
    float compressionRatio;

    public void encode(String filename) throws IOException {

        Path path = Paths.get("src", "main", "input_files", filename);
        BufferedReader file = new BufferedReader(new FileReader(path.toFile()));

        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int[] freq = new int[26];
        int uknownChars = 0;

        while(file.ready()) {
            boolean isChar = false;
            char next = (char)file.read();

            for (int i = 0; i < chars.length; ++i) {    
                next = Character.toLowerCase(next);

                if (next == chars[i]) {
                    ++freq[i];
                    isChar = true;
                }
            }

            if (!isChar) {
                ++uknownChars;
            }
        }

        int comprSize[] = new int[26];
        HuffTree tree = new HuffTree();
        tree.buildTree(chars, freq, 26);

        for (int i = 0; i < chars.length; ++i) {
            originalBits += 8 * freq[i];
            compressedBits += tree.getCode(chars[i]).length() * freq[i];
        }
        
        originalBits += 8 * uknownChars;
        compressedBits += 8 * uknownChars;
        compressionRatio = 100 * (originalBits - compressedBits) / (float)originalBits;
        file.close();
    }

    public static void main(String[] args) throws IOException {
        HuffCoder h = new HuffCoder();
        h.encode("input1");
        System.out.println("\n\n\nOriginal Bits:" + h.originalBits + "\nCompressed Bits:" + h.compressedBits + "\nRatio " + h.compressionRatio);
    }
}
