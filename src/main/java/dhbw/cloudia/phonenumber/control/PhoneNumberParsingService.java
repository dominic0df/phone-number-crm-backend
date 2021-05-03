package dhbw.cloudia.phonenumber.control;

import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberInputTO;
import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberOutputTO;
import dhbw.cloudia.phonenumber.control.exception.PhoneNumberParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for parsing phone number
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PhoneNumberParsingService {

    private static final String VALID_CHARACTERS = "0123456789 /-()[]+";
    private static final int AREA_CODE_LENGTH_ASSUMPTION_WITH_PREFIX = 4;
    private static final int AREA_CODE_LENGTH_ASSUMPTION_WITHOUT_PREFIX = 5;
    private static final String GERMAN_PREFIX = "49";
    private static final String PREFIX_WITH_BRACKETS_PATTERN = "^(\\[\\+[0-9]{2}].*)|(\\Q(\\E\\+[0-9]{2}\\Q)\\E.*)$";
    private static final String PREFIX_WITHOUT_BRACKETS_PATTERN = "^\\+[0-9]{2}.*$";
    private static final String SEPARATION_CHARACTERS_PATTERN = "-|/|[ ]+";
    private static final int MINIMUM_NUMBER_LENGTH_WITH_PREFIX = 8;
    private static final int MINIMUM_NUMBER_LENGTH_WITHOUT_PREFIX = 6;
    private static final int MINIMUM_AREA_CODE_LENGTH = 4;
    private static final int MINIMUM_PHONE_EXTENSION_LENGTH = 3;
    private static final String EMPTY_DEFAULT_PHONE_EXTENSION = "";
    public static final String PHONE_NUMBER_TOO_SHORT_ERROR_MESSAGE = "Phone number too short";
    public static final String USED_INVALID_CHARACTERS_ERROR_MESSAGE = "Used invalid characters";
    public static final String SEPARATION_ERROR_MESSAGE = "Separation error";

    /**
     * Service method that parses phone number into four parts
     * @param phoneNumberInputTO phone number input TO that contains phone number and default prefix as Strings
     * @return parsed phone number as phone number output TO
     */
    public PhoneNumberOutputTO parsePhoneNumber(PhoneNumberInputTO phoneNumberInputTO) {
        // Prepare method variables
        PhoneNumberOutputTO phoneNumberOutputTO = new PhoneNumberOutputTO();
        String phoneNumber = phoneNumberInputTO.getPhoneNumberString().trim();

        // validate characters in phone number
        checkIfOnlyValidCharacters(phoneNumber);

        // check for country prefix
        boolean hasCountryPrefix;
        if (phoneNumber.matches(PREFIX_WITH_BRACKETS_PATTERN)) {
            hasCountryPrefix = true;
            checkPhoneNumberLength(phoneNumber, true);
            phoneNumberOutputTO.setPrefixNumber(phoneNumber.substring(2, 4));
            phoneNumber = phoneNumber.substring(5).trim();
        } else if (phoneNumber.matches(PREFIX_WITHOUT_BRACKETS_PATTERN)){
            hasCountryPrefix = true;
            checkPhoneNumberLength(phoneNumber, true);
            phoneNumberOutputTO.setPrefixNumber(phoneNumber.substring(1, 3));
            phoneNumber = phoneNumber.substring(3).trim();
        } else {
            hasCountryPrefix = false;
            checkPhoneNumberLength(phoneNumber, false);
            phoneNumberOutputTO.setPrefixNumber(GERMAN_PREFIX);
        }

        // split remaining number at separation characters
        String[] splittedNumber = phoneNumber.split(SEPARATION_CHARACTERS_PATTERN);

        // if there are more than 3 remaining parts put them together and extract possible phone extension
        if (splittedNumber.length > 3) {
            String[] newSplittedNumber = new String[1];
            if (splittedNumber[splittedNumber.length-1].length() <= 3) {
                phoneNumberOutputTO.setPhoneExtension(splittedNumber[splittedNumber.length-1]);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < splittedNumber.length-1; i++) {
                    stringBuilder.append(splittedNumber[i]);
                }
                newSplittedNumber[0] = stringBuilder.toString();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : splittedNumber) {
                    stringBuilder.append(s);
                }
                newSplittedNumber[0] = stringBuilder.toString();
            }
            splittedNumber = newSplittedNumber;
        }

        // take splitted number and set phone number parts
        if (splittedNumber.length == 3) {
            phoneNumberOutputTO.setAreaCode(removeBrackets(splittedNumber[0]));
            phoneNumberOutputTO.setPhoneNumber(removeBrackets(splittedNumber[1]));
            checkAndSetPhoneExtension(phoneNumberOutputTO, removeBrackets(splittedNumber[2]));
            return phoneNumberOutputTO;
        } else if (splittedNumber.length == 2) {
            extractAreaCodeAndSetPhoneNumber(phoneNumberOutputTO, splittedNumber[0], hasCountryPrefix);
            checkAndSetPhoneExtension(phoneNumberOutputTO, removeBrackets(splittedNumber[1]));
        } else if (splittedNumber.length == 1) {
            extractAreaCodeAndSetPhoneNumber(phoneNumberOutputTO, splittedNumber[0], hasCountryPrefix);
            phoneNumberOutputTO.setPhoneExtension(EMPTY_DEFAULT_PHONE_EXTENSION);
        } else {
            throw new PhoneNumberParseException(SEPARATION_ERROR_MESSAGE);
        }
        return phoneNumberOutputTO;
    }

    private void checkIfOnlyValidCharacters(String number) {
        boolean isValid = true;
        for (int i = 0; i < number.length(); i++) {
            if (VALID_CHARACTERS.indexOf(number.charAt(i)) == -1) {
                isValid = false;
                break;
            }
        }
        if (!isValid) {
            throw new PhoneNumberParseException(USED_INVALID_CHARACTERS_ERROR_MESSAGE);
        }
    }

    private void checkPhoneNumberLength(String number, boolean hasCountryPrefix) {
        String cleanedNumber = removeBrackets(number);
        if ((hasCountryPrefix && cleanedNumber.length() < MINIMUM_NUMBER_LENGTH_WITH_PREFIX) ||
                (!hasCountryPrefix && cleanedNumber.length() < MINIMUM_NUMBER_LENGTH_WITHOUT_PREFIX)) {
            throw new PhoneNumberParseException(PHONE_NUMBER_TOO_SHORT_ERROR_MESSAGE);
        }
    }

    private void extractAreaCodeAndSetPhoneNumber(PhoneNumberOutputTO phoneNumberOutputTO, String numberString, boolean hasCountryPrefix) {
        if (numberString.contains(")")) {
            int areaCodeIndex = numberString.indexOf(")");
            phoneNumberOutputTO.setAreaCode(completeAreaCode(removeBrackets(numberString.substring(1, areaCodeIndex))));
            phoneNumberOutputTO.setPhoneNumber(removeBrackets(numberString.substring(areaCodeIndex+1)));
        } else {
            boolean hasShortAreaCode = hasShortAreaCode(numberString, hasCountryPrefix);
            if (hasCountryPrefix) {
                phoneNumberOutputTO.setAreaCode(removeBrackets(hasShortAreaCode ?
                        completeAreaCode(numberString) : numberString.substring(0, AREA_CODE_LENGTH_ASSUMPTION_WITH_PREFIX)));
                phoneNumberOutputTO.setPhoneNumber(removeBrackets(hasShortAreaCode ? "" : numberString.substring(AREA_CODE_LENGTH_ASSUMPTION_WITH_PREFIX)));
            } else {
                phoneNumberOutputTO.setAreaCode(removeBrackets(hasShortAreaCode ? completeAreaCode(numberString) : numberString.substring(0, AREA_CODE_LENGTH_ASSUMPTION_WITHOUT_PREFIX)));
                phoneNumberOutputTO.setPhoneNumber(removeBrackets(hasShortAreaCode ? "" : numberString.substring(AREA_CODE_LENGTH_ASSUMPTION_WITHOUT_PREFIX)));
            }
        }
    }

    private String completeAreaCode(String areaCode) {
        StringBuilder stringBuilder = new StringBuilder(areaCode);
        while (stringBuilder.length() < MINIMUM_AREA_CODE_LENGTH) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }

    private boolean hasShortAreaCode(String stringWithAreaCode, boolean hasCountryPrefix) {
        return stringWithAreaCode.length() < (hasCountryPrefix ? AREA_CODE_LENGTH_ASSUMPTION_WITH_PREFIX : AREA_CODE_LENGTH_ASSUMPTION_WITHOUT_PREFIX);
    }

    private void checkAndSetPhoneExtension(PhoneNumberOutputTO phoneNumberOutputTO, String possibleExtension) {
        if (possibleExtension.length() <= MINIMUM_PHONE_EXTENSION_LENGTH) {
            phoneNumberOutputTO.setPhoneExtension(possibleExtension);
        } else {
            phoneNumberOutputTO.setPhoneNumber(phoneNumberOutputTO.getPhoneNumber() + possibleExtension);
            phoneNumberOutputTO.setPhoneExtension(EMPTY_DEFAULT_PHONE_EXTENSION);
        }
    }

    private String removeBrackets(String number) {
        return number.replaceAll("\\Q[\\E|\\Q]\\E|\\Q(\\E|\\Q)\\E", "");
    }
}
