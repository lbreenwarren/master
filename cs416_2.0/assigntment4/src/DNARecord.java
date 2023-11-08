/**
 * The DNARecord class represents a DNA record containing a name and a profile.
 * @author Liam Warren
 * @version 11.7.23
 */
public class DNARecord {
    String name;
    Profile profile;

    /**
     * Creates a DNA record with the given name and profile.
     *
     * @param name    The name associated with the DNA record.
     * @param profile The profile containing counts of AGAT, AATG, and TATC repeats.
     */
    public DNARecord(String name, Profile profile) {
        this.name = name;
        this.profile = profile;
    }

    /**
     * Gets the name associated with the DNA record.
     *
     * @return The name of the DNA record.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name associated with the DNA record.
     *
     * @param name The name to set for the DNA record.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the profile of the DNA record containing counts of AGAT, AATG, and TATC repeats.
     *
     * @return The profile of the DNA record.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile of the DNA record containing counts of AGAT, AATG, and TATC repeats.
     *
     * @param profile The profile to set for the DNA record.
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Returns a string representation of the DNA record in the format "name: (AGAT = X, AATG = Y, TATC = Z)".
     *
     * @return A string representation of the DNA record.
     */
    @Override
    public String toString() {
        return name + ": " + profile.toString();
    }
}
