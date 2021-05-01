package dhbw.cloudia.phonenumber.control;

import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberInputTO;
import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberOutputTO;
import dhbw.cloudia.phonenumber.control.exception.PhoneNumberParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PhoneNumberParsingService {

    private static final Pattern PREFIX_PATTERN = Pattern.compile("(('0'|'+')[0-9][1-9]{2} | [\\(]('0'|'+')[0-9][1-9]{2}[\\)] | [\\[]('0'|'+')[0-9][1-9]{2}[\\[])", Pattern.CASE_INSENSITIVE);
    private static final Pattern AREA_CODE_PATTERN = Pattern.compile("[1-9]{3,4}", Pattern.CASE_INSENSITIVE);
    private static final Pattern AREA_CODE_PATTERN_WITHOUT_PREFIX = Pattern.compile("[1-9]{3,5}", Pattern.CASE_INSENSITIVE);
    private static final Pattern NUMBER_PATTERN = Pattern.compile("('/'|'-'|' ')?[1-9]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("('/'|'-'|' ')?[1-9]{1,3}", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PREFIX_PATTERN.pattern() +
                    AREA_CODE_PATTERN.pattern() +
                    NUMBER_PATTERN.pattern() +
                    EXTENSION_PATTERN.pattern(),
            Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_NUMBER_PATTERN_WITHOUT_PREFIX = Pattern.compile(
                    AREA_CODE_PATTERN_WITHOUT_PREFIX.pattern() +
                    NUMBER_PATTERN.pattern() +
                    EXTENSION_PATTERN.pattern(),
            Pattern.CASE_INSENSITIVE);
    public static final String COUNTRY_PREFIX_IDENTIFICATOR = "+";
    private static final String GERMAN_PREFIX = "49";

    public PhoneNumberOutputTO parsePhoneNumber(PhoneNumberInputTO phoneNumberInputTO) {
        PhoneNumberOutputTO phoneNumberOutputTO = new PhoneNumberOutputTO();
        String phoneNumber = phoneNumberInputTO.getPhoneNumberString().trim();
        boolean hasCountryPrefix;
        if (phoneNumber.matches("^(\\[\\+[0-9]{2}].*)|(\\Q(\\E\\+[0-9]{2}\\Q)\\E.*)$")) {
            phoneNumberOutputTO.setPrefixNumber(phoneNumber.substring(2, 4));
            phoneNumber = phoneNumber.substring(5).trim();
            hasCountryPrefix = true;
        } else if (phoneNumber.matches("^\\+[0-9]{2}.*$")){
            phoneNumberOutputTO.setPrefixNumber(phoneNumber.substring(1, 3));
            phoneNumber = phoneNumber.substring(3).trim();
            hasCountryPrefix = true;
        } else {
            phoneNumberOutputTO.setPrefixNumber(GERMAN_PREFIX);
            hasCountryPrefix = false;
        }
//        if (hasCountryPrefix) {
//            phoneNumberOutputTO.setPrefixNumber(phoneNumber.substring(0, 3));
//            phoneNumber = phoneNumber.substring(3).trim();
//        } else {
//            phoneNumberOutputTO.setPrefixNumber(GERMAN_PREFIX);
//        }
        String[] splittedNumber = phoneNumber.split("-|/|[ ]+");
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
        } else {
            throw new PhoneNumberParseException("Too many separation characters");
        }
        return phoneNumberOutputTO;
    }

    private void extractAreaCodeAndSetPhoneNumber(PhoneNumberOutputTO phoneNumberOutputTO, String numberString, boolean hasCountryPrefix) {
        if (numberString.contains(")")) {
            int areaCodeIndex = numberString.indexOf(")");
            phoneNumberOutputTO.setAreaCode(removeBrackets(numberString.substring(1, areaCodeIndex)));
            phoneNumberOutputTO.setPhoneNumber(numberString.substring(areaCodeIndex+1));
        } else {
            if (hasCountryPrefix) {
                phoneNumberOutputTO.setAreaCode(numberString.substring(0, 4));
                phoneNumberOutputTO.setPhoneNumber(numberString.substring(4));
            } else {
                phoneNumberOutputTO.setAreaCode(numberString.substring(0, 5));
                phoneNumberOutputTO.setPhoneNumber(numberString.substring(5));
            }
        }
    }

    private void checkAndSetPhoneExtension(PhoneNumberOutputTO phoneNumberOutputTO, String possibleExtension) {
        if (possibleExtension.length() <= 3) {
            phoneNumberOutputTO.setPhoneExtension(possibleExtension);
        } else {
            phoneNumberOutputTO.setPhoneNumber(phoneNumberOutputTO.getPhoneNumber() + possibleExtension);
        }
    }

    private String removeBrackets(String number) {
        if ((number.startsWith("(") && number.endsWith(")")) || (number.startsWith("[") && number.endsWith("]"))) {
            return number.substring(1, number.length()-1);
        } else if ((!number.startsWith("(") && !number.endsWith(")")) || (!number.startsWith("[") && !number.endsWith("]"))) {
           return number;
        } else {
            throw new PhoneNumberParseException("Flawed bracketed number");
        }
    }

//        String phoneNumber = phoneNumberInputTO.getPhoneNumberString().trim();
//        isPhoneNumberValid(phoneNumber);
//        PhoneNumberOutputTO phoneNumberOutputTO = PhoneNumberOutputTO.builder().build();
//
//        Matcher matcher = PREFIX_PATTERN.matcher(phoneNumber);
//        boolean prefixFound = matcher.find();
//        if (prefixFound) {
//            String oldString = phoneNumber;
//            phoneNumber = phoneNumber.replaceFirst(PREFIX_PATTERN.pattern(), "");
//            phoneNumberOutputTO.setPrefixNumber(oldString.substring(0, oldString.length() - phoneNumber.length()));
//        } else {
//            throw new PhoneNumberParseException("prefix of phone number could not be parsed: " + phoneNumber);
//        }
//
//        matcher = AREA_CODE_PATTERN.matcher(phoneNumber);
//        boolean areaCodeFound = matcher.find();
//        if (areaCodeFound) {
//            phoneNumberOutputTO.parseAreaCode(phoneNumber.split(AREA_CODE_PATTERN.pattern())[0]);
//            phoneNumber = phoneNumber.replaceFirst(AREA_CODE_PATTERN.pattern(), "");
//        } else {
//            throw new PhoneNumberParseException("area code of phone number could not be parsed: " + phoneNumber);
//        }
//
//        matcher = NUMBER_PATTERN.matcher(phoneNumber);
//        boolean numberPatternFound = matcher.find();
//        if (numberPatternFound) {
//            phoneNumberOutputTO.setPhoneNumber(phoneNumber.split(NUMBER_PATTERN.pattern())[0]);
//            phoneNumber = phoneNumber.replaceFirst(NUMBER_PATTERN.pattern(), "");
//        } else {
//            throw new PhoneNumberParseException("number pattern of phone number could not be parsed: " + phoneNumber);
//        }
//
//        matcher = EXTENSION_PATTERN.matcher(phoneNumber);
//        boolean extensionFound = matcher.find();
//        if (extensionFound) {
//            phoneNumberOutputTO.setPhoneExtension(phoneNumber.split(EXTENSION_PATTERN.pattern())[0]);
//        }
//
//        return phoneNumberOutputTO;
//    }
//
//    private Tuple<String, String> returnParsingSolution(Pattern pattern, String phoneNumber) {
//        Matcher matcher = pattern.matcher(phoneNumber);
//        boolean prefixFound = matcher.find();
//        if (prefixFound) {
//            String newPhoneElement = phoneNumber;
//            phoneNumber = phoneNumber.replaceFirst(PREFIX_PATTERN.pattern(), "");
//            newPhoneElement = newPhoneElement.substring(0, newPhoneElement.length() - phoneNumber.length());
//            return new Tuple<>(newPhoneElement, phoneNumber);
//        } else {
//            throw new PhoneNumberParseException("prefix of phone number could not be parsed: " + phoneNumber);
//        }
//    }
//
//    private boolean isPhoneNumberValid(String phoneNumber){
//        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
//        boolean phoneNumberMatch = matcher.find();
//        if(phoneNumberMatch) {
//            return true;
//        } else {
//            throw new PhoneNumberParseException("Phone number is not valid: " + phoneNumber);
//        }
//    }
}
