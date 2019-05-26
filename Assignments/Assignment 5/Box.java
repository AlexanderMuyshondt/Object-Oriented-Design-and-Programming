/*
 * Shipping Store Management Software v0.1
 * Developed for CS3354: Object Oriented Design and Programming.
 */

package shippingstore;

/**
 * Box class is a child class of Package
 */
public class Box extends Package {

    private int dimension;

    /**
     * Default constructor.
     */
    public Box() {
        this.dimension = 0;
    }

    /**
     * Constructor used to initialize the class fields of the class with the
     * provided values.
     * @param ptn package tracking number
     * @param specification detailed specification
     * @param mailingClass mailing class
     * @param dimension largest dimension
     * @param volume Volume
     */
    public Box(String ptn, String type, String specification, String mailingClass, 
            float weight, int volume, int dimension) {
        super(ptn, type, specification, mailingClass, weight, volume);
        this.dimension = dimension;
    }

    /**
     * Get the box dimension.
     * @return dimension
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * set the box dimension.
     * @param dimension box dimension
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Get the attributes of the box, in a formatted text fashion.
     * @return Formatted text
     */
    //@Override
    public String getFormattedText() {
        return String.format("| %12s | %12s | %13s | %13s | Dimension: %10d, Volume: %10d    | %n",
                "Box", ptn, specification, mailingClass, dimension, volume);
    }

    @Override
    public String toString() {
        return "Box{" + "ptn=" + ptn + ", specification=" + specification +
                ", Mailing Class=" + mailingClass + ", height=" + dimension +
                ", volume=" + volume + '}';
    }
}
