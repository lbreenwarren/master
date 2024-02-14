/** A program to manage a checking account.
 * @author Liam Warren
 * @version 2/14/22
 * */
public class CheckingAccount implements Account {
    int accountbalance;
    String id;
    String name;

    /**Constructor for CheckingAccount class.
     * @param id input id
     * @param name input name
     * @param startingbalance initial account balance
     * */
    public CheckingAccount(String id, String name, int startingbalance) {
        accountbalance = startingbalance;
        this.name = name;
        this.id = id;
    }

    /**Increases account balance by deposit ammount.
     * @param amount amount by which to increase accountbalance*/
    public void deposit(int amount) {
        accountbalance = accountbalance + amount;
    }


    /**Decreases accountbalance by withdrawl amount if there is enough money in accouunt.
     * @param amount amount by which to decrease accountbalance
     * @return true if succesfull withdrawl, false if not enough money*/

    public boolean withdraw(int amount) {
        if (amount > accountbalance) {
            return false;
        } else {
            accountbalance = accountbalance - amount;
            return true;
        }
    }

    /**getter for accountbalance.
     * @return value of accountbalance*/
    public int getBalance() {
        return accountbalance;
    }

    /**gettter for id.
     * @return id number as string*/
    public String getId() {
        return id;
    }

    /**getter for name.
     * @return string containing name*/
    public String getName() {
        return name;
    }

    /**toString function.
     * @return string containing account information*/
    public String toString() {
        return id + " " + name + " $" + accountbalance;
    }
}
