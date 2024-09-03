
import java.util.ArrayList;
import java.util.Random;

import static mystery.Sorts.*;

public class Detective {

    public static void main(String[] args) {

        ArrayList<Integer> arr = new ArrayList<>();
        Random rand = new Random(0);

        // for(int i = 0; i < 500; i++) {
        //     arr.add(rand.nextInt(50));
        // }
        while (arr.size() < 500) {
            arr.add(rand.nextInt(500));
        }

        System.out.println("original:");
        System.out.println(arr.toString());

        // sort4(arr, -1);

        long start, finish;
        double elapsed;
        start = System.nanoTime();
        //interrupt sort after 150 iterations of the sort algorithm
        sort5(arr, 250);
        finish = System.nanoTime();
        elapsed = (finish - start) / 1000.0;
        System.out.println("Elapsed Time:" + elapsed);
        double OnTime = elapsed / arr.size();
        System.out.println("Efficiency:" + OnTime);
        System.out.println("current:");
        for (int i = 0; i < arr.size(); ++i) {
            if (i % 10 == 0) {
                System.out.println("(" + i + ")");
            }
            System.out.print(arr.get(i) + " ");
        }

    }
}
