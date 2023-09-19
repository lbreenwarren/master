/**
 * @author author
 * @version version
 */
public class CheckingAccount implements Account {
    
    int balance;
    String accountId;
    String name;

    /**constructor for CheckingAccount.
     * @param id account ID
     * @param name account name
     * @param startingBalance account balance at construction
     */
    public CheckingAccount(String id, String name, int startingBalance) {
        accountId = id;
        this.name = name;
        balance = startingBalance;
    }

    /** deposit adds money to the account.
    * @param amount - the amount of the deposit, a nonnegative integer  */
    public void deposit(int amount) {
        balance = balance + amount;
    }

    /** withdraw deducts money from the account, if possible.
     * @param amount - the amount of the withdrawal, a nonnegative integer
     * @return true, if the the withdrawal was successful;
     *  return false, otherwise.  */
    public boolean withdraw(int amount) {
        if (balance - amount >= 0) {
            balance =  balance - amount;
            return true;
        } else {
            return false;
        }
    }

    /**getter for balance.
     * @return balance
     */
    public int getBalance() {
        return balance;
    }

    /**getter for accountID.
     * 
     * @return accountId
     */
    public String getId() {
        return accountId;
    }

    /**getter for account name.
     * 
     * @return account name
     */
    public String getName() {
        return name;
    }

    /**toString method for CheckingAccount.
     * @return CheckingAccount info
     */
    public String toString() {
        return accountId + " " + name + " $" + balance;
    }
}
