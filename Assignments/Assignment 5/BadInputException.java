/*
 * Shipping Store Management Software v0.1
 * Developed for CS3354: Object Oriented Design and Programming.
 */
package shippingstore;

/**
 * Custom Exception type, used to report bad input from user.
 */
public class BadInputException extends Exception {

    /**
     * Constructor, allows custom message assignment for thrown exception.
     * @param message 
     */
    public BadInputException(String message) {
        super(message);
    }
}
