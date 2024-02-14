/**
 * The Profile class represents a profile containing counts of AGAT, AATG, and TATC repeats.
 * @author Liam Warren
 * @version 11.7.23
 */
public class Profile {
    int agatCount;
    int aatgCount;
    int tatcCount;

    /**
     * Gets the count of AGAT repeats in the profile.
     *
     * @return The count of AGAT repeats.
     */
    public int getAgatCount() {
        return agatCount;
    }

    /**
     * Sets the count of AGAT repeats in the profile.
     *
     * @param agatCount The count of AGAT repeats to set.
     */
    public void setAgatCount(int agatCount) {
        this.agatCount = agatCount;
    }

    /**
     * Gets the count of AATG repeats in the profile.
     *
     * @return The count of AATG repeats.
     */
    public int getAatgCount() {
        return aatgCount;
    }

    /**
     * Sets the count of AATG repeats in the profile.
     *
     * @param aatgCount The count of AATG repeats to set.
     */
    public void setAatgCount(int aatgCount) {
        this.aatgCount = aatgCount;
    }

    /**
     * Gets the count of TATC repeats in the profile.
     *
     * @return The count of TATC repeats.
     */
    public int getTatcCount() {
        return tatcCount;
    }

    /**
     * Sets the count of TATC repeats in the profile.
     *
     * @param tatcCount The count of TATC repeats to set.
     */
    public void setTatcCount(int tatcCount) {
        this.tatcCount = tatcCount;
    }

    /**
     * Compares this Profile object to another object for equality.
     *
     * @param o The object to compare to this Profile.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        Profile prof = (Profile) o;
        if (agatCount == prof.getAgatCount() && aatgCount == prof.getAatgCount() && tatcCount == prof.getTatcCount()) {
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the Profile object.
     *
     * @return A string in the format "(AGAT = X, AATG = Y, TATC = Z)" where X, Y, and Z are the respective counts.
     */
    @Override
    public String toString() {
        return "(AGAT = " + agatCount + ", AATG = " + aatgCount + ", TATC = " + tatcCount + ")";
    }
}
