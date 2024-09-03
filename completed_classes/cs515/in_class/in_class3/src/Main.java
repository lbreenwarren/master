import java.util.Iterator; 

import java.util.SortedSet; 

import java.util.TreeSet; 

 

public class Main { 

     

    public static void main(String[] args) { 

        SortedSet<String> s = new TreeSet<>(); 

        boolean result; 

 

        s.add("There"); s.add("World"); s.add("World"); 

 

        result = s.add("Hello"); 

        System.out.println("insert successful? " + result); 

 

        result = s.add("Hello"); 

        System.out.println("insert successful? " + result); 

 

        System.out.print("Set contains:"); 

        for (String item : s) { 

            System.out.print(" " + item); 

        } 

        System.out.println(); 

 

        System.out.print("Set contains:"); 

        Iterator<String> it = s.iterator(); 

        while(it.hasNext()) { 

            System.out.print(" " + it.next()); 

        } 

        System.out.println(); 

    } 

} 