
/**
* A class to model a CPU in a simple Virtual Machine.
*@author Liam Warren
*@version 10.10.23
*/
public class CPU {

    // Registers
    private int iP;
    private int iS; 
    private int r0; 
    private int r1; 

    /**
     * Initializes the CPU by setting registers to 0.
     */
    public CPU() {
        iP = 0;
        iS = 0;
        r0 = 0;
        r1 = 0;
    }

    /**
     * Executes instructions from memory and advances the program counter.
     *
     * @param mem        The memory object containing instructions.
     * @param debugMode  Indicates whether to print debug string.
     * @throws IllegalStateException if an address is out of bounds.
     */
    public void run(Memory mem, boolean debugMode) throws IllegalStateException {
        int step = 0;
        boolean halt = false;
        iP = 0;
        iS = 0;

        while (!halt) {
            iS = mem.memory[iP];

            if (debugMode) {
                System.out.println("STEP " + step + ":");
                System.out.printf("IP=%2d, IS=%2d, R0=%2d, R1=%2d", iP, iS, r0, r1);
                System.out.println(mem);
                System.out.println("----------");
            }

            switch (iS) {
                case 0:
                    halt = true;
                    break;
                case 1:
                    r0 = (r0 + r1) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 2:
                    r0 = (r0 - r1) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 3:
                    r0 = (r0 + 1) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 4:
                    r1 = (r1 + 1) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 5:
                    r0 = (r0 - 1) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 6:
                    r1 = (r1 - 1) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 7:
                    Beep.beep();
                    iP = (iP + 1) & 0xf;
                    break;
                case 8:
                    iP = (iP + 1) & 0xf;
                    System.out.println(mem.memory[iP]);
                    iP = (iP + 1) & 0xf;
                    break;
                case 9:
                    iP = (iP + 1) & 0xf;
                    r0 = (mem.memory[mem.memory[iP]]) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 10:
                    iP = (iP + 1) & 0xf;
                    r1 = (mem.memory[mem.memory[iP]]) & 0xf;
                    iP = (iP + 1) & 0xf;
                    break;
                case 11:
                    iP = (iP + 1) & 0xf;
                    mem.memory[mem.memory[iP]] = r0;
                    iP = (iP + 1) & 0xf;
                    break;
                case 12:
                    iP = (iP + 1) & 0xf;
                    mem.memory[mem.memory[iP]] = r1;
                    iP = (iP + 1) & 0xf;
                    break;
                case 13:
                    iP = (mem.memory[(iP + 1) & 0xf]) & 0xf;
                    break;
                case 14:
                    if (r0 == 0) {
                        iP = (mem.memory[(iP + 1) & 0xf]) & 0xf;
                    } else {
                        iP = (iP + 1) & 0xf;
                        iP = (iP + 1) & 0xf;
                    }
                    break;
                case 15:
                    if (r0 != 0) {
                        iP = mem.memory[(iP + 1) & 0xf];
                    } else {
                        iP = (iP + 1) & 0xf;
                        iP = (iP + 1) & 0xf;
                    }
                    break;

            }

            ++step;
        }
    }

    /**
    * Returns a string representation of the CPU's state.
    *
    * @return A string containing the values of iP, iS, r0, and r1.
    */
    public String toString() {
        String ret = String.format("IP=%2d, IS=%2d, R0=%2d, R1=%2d", iP, iS, r0, r1);
        return ret;
    }
}
