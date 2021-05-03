package dhbw.cloudia.phonenumber.control.exception;

/**
 * Exception class for parsing errors when parsing a phone number
 */
public class PhoneNumberParseException extends RuntimeException {
    public PhoneNumberParseException(String message) {
        super(message);
    }
}
