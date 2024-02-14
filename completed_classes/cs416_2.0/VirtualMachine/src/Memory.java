import java.util.Scanner;

/**
 * A class to model a Simple Virtual machine Memory.
 *@author Liam Warren
 *@version 10.10.23
 */
public class Memory {

    int[] memory;

    /**
     * Initializes a new memory object with a default size of 16 and initializes all
     * memory locations to 0.
     */
    public Memory() {
        memory = new int[16];
        for (int i = 0; i < 16; ++i) {
            memory[i] = 0;
        }
    }

    /**
     * Initializes a new memory object with a fixed size of 16 and populates memory
     * locations with values from the provided Scanner.
     *
     * @param s The Scanner that reads instructions to populate memory with.
     */
    public Memory(Scanner s) {
        memory = new int[16];
        for (int i = 0; i < memory.length; ++i) {
            write(i, s.nextInt());
        }
    }

    /**
     * Reads the value at the specified memory address.
     *
     * @param address The memory address to read from.
     * @return The value stored at the specified memory address.
     * @throws IllegalStateException if the provided address is out of bounds.
     */
    public int read(int address) throws IllegalStateException {
        if (address < 0 || address >= memory.length) {
            throw new IllegalStateException("Address out of bounds");
        } else {
            return memory[address];
        }
    }

    /**
     * Writes a value to the specified memory address.
     *
     * @param address The memory address to write to.
     * @param value   The value to be written to the memory address.
     * @throws IllegalStateException if the provided address is out of bounds.
     */
    public void write(int address, int value) throws IllegalStateException {
        if (address < 0 || address >= memory.length) {
            throw new IllegalStateException("Address out of bounds");
        } else {
            memory[address] = value;
        }
    }

    /**
     * Returns a string representation of the memory contents, displaying both
     * memory addresses and their corresponding values.
     *
     * @return A string representation of the memory contents.
     */
    public String toString() {
        String ret = "address: ";

        for (int i = 0; i < memory.length; ++i) {
            ret += "|  " + i + " ";
        }

        ret += "|\n  value: ";

        for (int i = 0; i < memory.length; ++i) {
            ret += "|  " + memory[i] + " ";
        }

        ret += "|";
        return ret;
    }

    /**
     * Returns the raw memory array for direct access.
     *
     * @return The internal memory array.
     */
    public int[] rawMemory() {
        return memory;
    }
}
