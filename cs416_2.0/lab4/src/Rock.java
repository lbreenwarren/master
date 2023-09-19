/** A program which stores infomation about a rock in a class and calculates density.
 * @author Liam Warren
 * @version 9.13.23
 */
public class Rock {
    String name;
    double weight;
    double volume;

    /** Constructor for Rock class.
    * @param name name of rock
    */
    public Rock(String name) {
        this.name = name;
    }

    /**Getter for name field.
     * @return name field of Rock
     */
    public String getName() {
        return name;
    }

    /**getter for weight field.
     * @return weight field of Rock
     */
    public double getNumPounds() {
        return weight;
    }

    /** Getter for volume field.
     * @return volume field of Rock
    */
    public double getVolume() {
        return volume;
    }

    /**Setter for name field.
     * @param name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /** setter for weight field.
     * @param weight to be set
     */
    public void setNumPounds(double weight) {
        this.weight = weight;
    }

    /**setter for volume field.
     * @param volume to be set
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /** function to calculate Density.
     * @return density of rock
     */
    public int calculateDensity() {
        return (int) (weight / volume);
    }

    /** function to increase weight field.
     * @param value by which to increase weight
     */
    public void increasePounds(double value) {
        weight = weight + value;
    }

    /** function to decrease weight field.
    * @param value by which to decrease weight
    */
    public void decreasePounds(double value) {
        weight = weight - value;
    }

    /** ToString method.
     * @return info string
     */
    public String toString() {
        String toString = String.format("Rock %s weighs %.3f pounds with a" 
            + " density of %d", name, weight, calculateDensity());
        return toString;
    }
}