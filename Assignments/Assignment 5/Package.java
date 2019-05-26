/*
 * Shipping Store Management Software v0.1
 * Developed for CS3354: Object Oriented Design and Programming.
 */

package shippingstore;

import java.io.Serializable;

/**
 * Class Package is an abstract entity class that represents a Package in the
 * shipping store. This class is expected to be inherited by subclasses
 * that specify the package type. No instance of this class can be created.
 * 
 */
public class Package implements Serializable {

    /**
     * The Package Tracking Number (PTN) of the vehicle.
     */
    protected String ptn;

    /**
     * The specification  of the package.
     */
    protected String specification;
    
    /**
     * The type of the package.
     */
    protected String type;

    /**
     * The mailing class of the package.
     */
    protected String mailingClass;
    
    /**
     * The weight of the package.
     */
    protected float weight;
    
    /**
     * The volume of the package.
     */
    protected int volume;

    /**
     * Default constructor used to initialize the class fields of the class.
     * Since this is an abstract class, the constructor cannot be used to
     * instantiate objects object of the class.
     */
    protected Package() {
        this.ptn = "";
        this.specification = "";
        this.mailingClass = "";
        this.type = "";
        this.weight = weight;
        this.volume = volume;
    }

    /**
     * This constructor initializes the package order object. The constructor provides no
     * user input validation. That should be handled by the class that creates a
     * package order object.
     *
     * @param trackingnumber a <b><CODE>String</CODE></b> that represents the tracking number
     *
     * @param type a <b><CODE>String</CODE></b> that represents the type.
     * Types: Postcard, Letter, Envelope, Packet, Box, Crate, Drum, Roll, Tube.
     *
     * @param specification a <b><CODE>String</CODE></b> that represents the specification.
     * Specification: Fragile, Books, Catalogs, Do-not-Bend, N/A - one per package
     *
     * @param mailingclass a <b><CODE>String</CODE></b> that represents the mailing class
     * Mailing class: First-Class, Priority, Retail, Ground, Metro.
     *
     * @param weight a <b><CODE>float</CODE></b> that represents the weight of the package in oz
     *
     * @param volume an <b><CODE>int</CODE></b> that represents the volume of the package in
     * cubic inches, calculated as Width x Length x Height
     *
     */
    protected Package(String ptn, String type, String specification, String mailingClass, 
            float weight, int volume) {
        this.ptn = ptn;
        this.type = type;
        this.specification = specification;
        this.mailingClass = mailingClass;
        this.weight = weight;
        this.volume = volume;
    }

    /**
     * Get the PTN of the package.
     * @return ptn The PTN of the package.
     */
    public String getPtn() {
        return ptn;
    }

    /**
     * Set the PTN of the package.
     * @param ptn
     */
    public void setPtn(String ptn) {
        this.ptn = ptn;
    }

    /**
     * Get the specification of the package.
     * @return specification The Specification of the package
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * Set the specification of the package.
     * @param specification
     */
    public void setSpecification(String specification) {
        this.specification = specification;
    }

    /**
     * Get the mailing class of the package.
     * @return mailingclass The mailing class of the package
     */
    public String getMailingClass() {
        return mailingClass;
    }

    /**
     * Set the mailingClass of the package.
     * @param mailingClass
     */
    public void setMailingClass(String mailingClass) {
        this.mailingClass = mailingClass;
    }
    
    /**
     * Get the type of package.
     * @return type The type of package
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the package.
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get the Weight of the package.
     * @return weight The weight of the package
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set the weight of the package.
     * @param weight
     */
    public void setMailingClass(float weight) {
        this.weight = weight;
    }
    
    /**
     * Get the volume of the package.
     * @return volume The mailing class of the package
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Set the volume of the package.
     * @param volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }
    
    /**
     * Abstract method, to be implemented by subclasses of class Package.
     * @return the text describing the package in a formatted manner.
     */
    public String getFormattedText() {
        return String.format("| %12s | %12s | %13s | %13s | Weight: %10s, Volume: %10f  | %n",
                type, ptn, specification, mailingClass, weight, volume);
    }
}
