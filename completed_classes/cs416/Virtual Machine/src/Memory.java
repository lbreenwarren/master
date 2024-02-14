import java.util.Arrays;
import java.util.Scanner;

/**
 * A class to model a Simple Virtual machine Memory
 *
 */
public class Memory {

    int[] memory = new int[16];
    public Memory() {
        memory = new int [16];
    }

    public Memory(Scanner s) {
        for (int i = 0; i < memory.length; ++i) {
            write(i, s.nextInt());
        }

    }

    public int read(int address) throws ArrayIndexOutOfBoundsException {
        try {
            return memory [address];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException();
        }
    }

    public void write(int address, int value) throws ArrayIndexOutOfBoundsException {
        try {
            memory[address] = value;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException();
        }
    }

    public String toString() {
        String address = "";
        String value = "";

        for (int i = 0; i < memory.length; ++i) {
            address += "" + String.format("%3d", i) + " |";
            value += "" + String.format("%3d", memory[i]) + " |";
        }

        return "address: |" + address + "\n  value: |" + value;
    }

    public int[] rawMemory() {
        return memory;
    }
}
