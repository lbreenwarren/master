import java.time.LocalDate;

/**
 * An abstract class to represent items which can be checked out from a library.
 *
 * @author Sofia Lemons
 * @version 1.0
 */
public abstract class Item implements Printable {
    private LocalDate dueDate;
    private boolean checkedOut;

    /**
     * Returns the due date for this item, or null if due date is not set.
     *
     * @return LocalDate The due date.
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Changes checked out status to true.
     */
    public void setCheckedOut() {
        checkedOut = true;
    }

    /**
     * Sets due date for this item.
     *
     * @param dueDate The new due date.
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Changes the checked out status of the item to true and sets the due date.
     *
     * @param currDate The current date.
     */
    public abstract void checkOut(LocalDate currDate);

    /**
     * Changes the checked out status of the item to false and sets the due date to null.
     */
    public void checkIn() {
        checkedOut = false;
        dueDate = null;
    }

    /**
     * Indicates whether the item is checked out.
     *
     * @return boolean True if item is checked out, false otherwise.
     */
    public boolean isCheckedOut() {
        return checkedOut;
    }

    /**
     * Checks whether the search text is contained in any of this item's fields.
     * Must be case-insensitive.
     *
     * @param search The text to search for.
     * @return boolean True if any of this item's fields contain the search text, false otherwise.
     */
    public abstract boolean contains(String search);

}
