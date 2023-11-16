import java.util.Scanner;

public class Towers {


    public static void towersOfHanoi(int disks, int from, int to, int spare) {
        if (disks == 1) {
            System.out.printf("Move 1 disk from stack %d to stack %d\n", from, to);
        } else {
            // move n -1 discs off of the biggest
            towersOfHanoi(disks - 1, from, spare, to);
            // move biggest to correct stack
            System.out.printf("move 1 disc from stack %d to stack %d\n", from, to);
            // move n-1 discs from spare to correct stack
            towersOfHanoi(disks - 1, spare, to, from);

        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N;
        System.out.println("How many disks are to be moved from stack 0 to stack 1?\n");
        N = in.nextInt();
        towersOfHanoi(N, 0, 1, 2);
        System.out.println("Done");
    }
}