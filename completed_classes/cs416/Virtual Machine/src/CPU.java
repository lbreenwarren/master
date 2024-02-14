
/**
* A class to model a CPU in a simple Virtual Machine
*/
public class CPU {
    int IP;
    int IS;
    int R0;
    int R1;
    public CPU() {
        IP = 16 & 0xF;
        IS = 16 & 0xF;
        R0 = 16 & 0xF;
        R1 = 16 & 0xF;
    }

    public void run(Memory mem, boolean debugMode) throws IllegalStateException {
        IS = mem.memory[IP];
        int i = 0;

        if(IS == 0 && debugMode) {
            System.out.printf("STEP %d:\nIP= %d, IS= %d, R0= %d, R1= %d\n", i, IP, IS, R0, R1);
            System.out.println(mem);
            System.out.println("----------");
        }

        while(IS != 0 && IP <= 15){
            IS = mem.memory[IP];
            if (debugMode) {
                System.out.printf("STEP %d:\nIP= %d, IS= %d, R0= %d, R1= %d\n", i, IP, IS, R0, R1);
                System.out.println(mem);
                System.out.println("----------");
            }
            switch (IS) {
                case 0:
                    break;
                case 1:
                    R0 = (R0 + R1) & 0xf;
                    ++IP;
                    break;
                case 2:
                    R0 = (R0 - R1) & 0xf;
                    ++IP;
                    break;
                case 3:
                    R0 = (R0 + 1) & 0xf;
                    ++IP;
                    break;
                case 4:
                    R1 = (R1 + 1) & 0xf;
                    ++IP;
                    break;
                case 5:
                    R0 = (R0 - 1) & 0xf;
                    ++IP;
                    break;
                case 6:
                    R1 = (R1 - 1) & 0xf;
                    ++IP;
                    break;
                case 7:
                    Beep.beep();
                    ++IP;
                    break;
                case 8:
                    System.out.println(mem.read(IP + 1));
                    ++IP;
                    ++IP;
                    break;

                case 9:
                    R0 = mem.read(mem.read(IP + 1)) & 0xf;
                    ++IP;
                    ++IP;
                    break;
                case 10:
                    R1 = (mem.read(mem.read(IP + 1))) & 0xf;
                    ++IP;
                    ++IP;
                    break;
                case 11:
                    mem.write(mem.read(IP + 1), R0);
                    ++IP;
                    ++IP;
                    break;
                case 12:
                    mem.write(mem.read(IP + 1), R1);
                    ++IP;
                    ++IP;
                    break;
                case 13:
                    IP = mem.read(IP + 1);
                    break;
                case 14:
                    if (R0 == 0) {
                        IP = mem.read(IP + 1);
                        break;
                    }
                    else {
                        ++IP;
                        ++IP;
                        break;

                    }
                case 15:
                    if (R0 != 0) {
                        IP = mem.read(IP + 1);
                        break;
                    }
                    else {
                        ++IP;
                        ++IP;
                        break;

                    }

            }
            ++i;
        }
    }
    public String toString() {
        return "IP= " + IP + ", IS= " + IS + ", R0=" + R0 + ", R1=" + R1;
    }
}