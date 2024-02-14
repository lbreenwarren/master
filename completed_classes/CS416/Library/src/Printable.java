/**
 * Interface for providing simple and detailed text representations of a class' data.
 *
 * @author Sofia Lemons
 * @version 1.0
 */
public interface Printable {

    /**
     * Provides a simple representation of this object's data.
     *
     * @return String A simple representation of this object.
     */
    String basicInfo();

    /**
     * Provides a detailed representation of this object's data.
     *
     * @return String A detailed representation of this object.
     */
    String detailedInfo();

}
