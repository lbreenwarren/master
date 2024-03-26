package main.set;

import java.util.HashMap;
import java.util.List;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BufferedReader;


public class path {
    
    public HashMap<Character, List<Character>> readFile(String fileName) {
           
        Path path = Paths.get("src", "main", "input_files", filename);
        java.io.BufferedReader file = new BufferedReader(new FileReader(path.toFile()));

        HashMap<Character, List<Character>> ret = new HashMap<Character, List<Character>>();
        int maxDepth = 0;

        while (file.ready()) {
            char firstChar = file.read();
            if (firstChar == '?') {
                char fromChar = file.read();
                char toChar = file.read();
                ret.forEach((key,val) -> {
                    if (val.size() >= maxDepth) {
                        
                    }
                });

            } else {
                if (ret.containsKey(firstChar)) {
                    char secChar = file.read();
                    ret.get(firstChar).add(secChar);
                    ++maxDepth;
                } else {
                    char secChar = file.read();
                    List<Character> val = new List<Character>();
                    val.add(secChar);
                    ret.put(firstChar, val);
                    if (maxDepth == 0) {
                        maxDepth = 1;
                    }
                }
            }
        }


    }
}
